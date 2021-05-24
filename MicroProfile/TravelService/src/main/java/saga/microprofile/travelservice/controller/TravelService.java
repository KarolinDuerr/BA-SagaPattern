package saga.microprofile.travelservice.controller;

import saga.microprofile.travelservice.error.*;
import saga.microprofile.travelservice.model.*;
import saga.microprofile.travelservice.saga.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
@TravelServiceImpl
public class TravelService implements ITravelService {

    private static final Logger logger = Logger.getLogger(TravelService.class.toString());

//    @Autowired
    private final TripInformationRepository tripInformationRepository;

//    @Autowired
//    private final SagaInstanceFactory sagaInstanceFactory; // TODO

    public TravelService(final TripInformationRepository tripInformationRepository) {
        this.tripInformationRepository = tripInformationRepository;
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

        TripInformation tripInformation = tripInformationRepository.findById(tripId);

        if (tripInformation == null) {
            String message = String.format("The trip booking (ID: %d) does not exist.", tripId);
            logger.info(message);
            throw new TravelException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return tripInformation;
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
        // TODO
//        sagaInstanceFactory.startBookTripSaga(bookTripSagaData);

        return tripInformation;
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
}
