package saga.netflix.conductor.travelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saga.netflix.conductor.travelservice.api.dto.FailureType;
import saga.netflix.conductor.travelservice.error.BookingNotFound;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netflix.conductor.travelservice.error.TravelException;
import saga.netflix.conductor.travelservice.model.BookingStatus;
import saga.netflix.conductor.travelservice.model.RejectionReason;
import saga.netflix.conductor.travelservice.model.TripInformation;
import saga.netflix.conductor.travelservice.model.TripInformationRepository;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;
import saga.netflix.conductor.travelservice.saga.cancelTripSaga.CancelTripSagaData;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("TravelService")
public class TravelService implements ITravelService {

    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);

    @Autowired
    private final TripInformationRepository tripInformationRepository;

    @Autowired
    private final SagaInstanceFactory sagaInstanceFactory;

    public TravelService(final TripInformationRepository tripInformationRepository,
                         final SagaInstanceFactory sagaInstanceFactory) {
        this.tripInformationRepository = tripInformationRepository;
        this.sagaInstanceFactory = sagaInstanceFactory;
    }

    @Override
    public List<TripInformation> getTripsInformation() {
        logger.info("Get trip bookings from Repository.");

        List<TripInformation> trips = new LinkedList<>();

        Iterable<TripInformation> tripsInformation = tripInformationRepository.findAll();

        for (TripInformation tripInformation : tripsInformation) {
            trips.add(tripInformation);
        }

        return trips;
    }

    @Override
    public TripInformation getTripInformation(final Long tripId) throws TravelException {
        logger.info(String.format("Get trip booking (ID: %d) from Repository.", tripId));

        Optional<TripInformation> tripInformation = tripInformationRepository.findById(tripId);

        if (!tripInformation.isPresent()) {
            String message = String.format("The trip booking (ID: %d) does not exist.", tripId);
            logger.info(message);
            throw new TravelException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return tripInformation.get();
    }

    @Override
    public TripInformation bookTrip(final TripInformation tripInformation) {
        logger.info("Saving the booked Trip: " + tripInformation);

        // ensure idempotence of trip bookings
        TripInformation alreadyExistingTripBooking = checkIfBookingAlreadyExists(tripInformation);
        if (alreadyExistingTripBooking != null) {
            return alreadyExistingTripBooking;
        }

        tripInformationRepository.save(tripInformation);

        // create and start the BookTripSaga with necessary information
        BookTripSagaData bookTripSagaData = new BookTripSagaData(tripInformation.getId(), tripInformation);
        sagaInstanceFactory.startBookTripSaga(bookTripSagaData);

        return tripInformation;
    }

    @Override
    public BookingStatus cancelTrip(final Long tripId, final Long customerId, final FailureType provokeFailureType) throws TravelException {
        logger.info("Starting to cancel the Trip: " + tripId);

        // ensure idempotence of trip bookings
        TripInformation tripBooking = getTripInformation(tripId);

        if (tripBooking.getBookingStatus() == BookingStatus.CANCELLED || tripBooking.getBookingStatus() == BookingStatus.CANCELLING) {
            logger.info("Trip has already been cancelled: " + tripBooking);
            return tripBooking.getBookingStatus();
        }

        prepareFailureIfRequested(tripBooking, provokeFailureType);

        tripBooking.cancel();
        tripInformationRepository.save(tripBooking);

        // create the BookTripSaga with the necessary information
        CancelTripSagaData cancelTripSaga = new CancelTripSagaData(tripId, tripBooking.getHotelId(),
                tripBooking.getFlightId(), customerId);
        sagaInstanceFactory.startCancelTripSaga(cancelTripSaga);

        return tripBooking.getBookingStatus();
    }

    @Override
    public void rejectTrip(final Long tripId, final RejectionReason rejectionReason) {
        logger.info("Rejecting the booked trip with ID " + tripId);

        try {
            TripInformation tripInformation = getTripInformation(tripId);

            BookingStatus newBookingStatus = convertToBookingStatus(rejectionReason);
            tripInformation.reject(newBookingStatus);
            tripInformationRepository.save(tripInformation);
        } catch (TravelException exception) {
            throw new BookingNotFound(tripId);
        }
    }

    @Override
    public void confirmTripBooking(final Long tripId, final long hotelId, final long flightId) {
        logger.info("Confirming the booked trip with ID " + tripId);

        try {
            TripInformation tripInformation = getTripInformation(tripId);

            tripInformation.setHotelId(hotelId);
            tripInformation.setFlightId(flightId);
            tripInformation.confirm();
            tripInformationRepository.save(tripInformation);
        } catch (TravelException exception) {
            throw new BookingNotFound(tripId);
        }
    }

    // ensure idempotence of trip bookings
    private TripInformation checkIfBookingAlreadyExists(final TripInformation tripInformation) {
        List<TripInformation> customerTrips =
                tripInformationRepository.findByTravellerName(tripInformation.getTravellerName());

        Optional<TripInformation> savedTripBooking =
                customerTrips.stream().filter(tripInfo -> tripInfo.equals(tripInformation)).findFirst();

        if (!savedTripBooking.isPresent()) {
            return null;
        }

        logger.info("Trip has already been booked: " + savedTripBooking.toString());
        return savedTripBooking.get();
    }

    private BookingStatus convertToBookingStatus(final RejectionReason rejectionReason) {
        switch (rejectionReason) {
            case NO_HOTEL_AVAILABLE:
                return BookingStatus.REJECTED_NO_HOTEL_AVAILABLE;
            case NO_FLIGHT_AVAILABLE:
                return BookingStatus.REJECTED_NO_FLIGHT_AVAILABLE;
            default:
                return BookingStatus.REJECTED_UNKNOWN;
        }
    }

    private void prepareFailureIfRequested(final TripInformation tripInformation, final FailureType provokeFailureType) {
        switch (provokeFailureType) {
            case HOTEL_FAILURE:
                tripInformation.setHotelId(-1);
                break;
            case FLIGHT_FAILURE:
                tripInformation.setFlightId(-1);
                break;
            default:
                // successful execution requested
                break;
        }
    }
}
