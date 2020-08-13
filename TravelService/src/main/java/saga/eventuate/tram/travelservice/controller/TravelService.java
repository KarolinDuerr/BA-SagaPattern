package saga.eventuate.tram.travelservice.controller;

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.travelservice.error.ErrorType;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.BookingStatus;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.model.TripInformationRepository;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;
import saga.eventuate.tram.travelservice.saga.BookTripSagaData;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("TravelService")
@Transactional
public class TravelService implements  ITravelService {

    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);

    @Autowired
    private final TripInformationRepository tripInformationRepository;

    @Autowired
    private final SagaInstanceFactory sagaInstanceFactory;

    @Autowired
    private final BookTripSaga bookTripSaga;

    public TravelService(final TripInformationRepository tripInformationRepository, final SagaInstanceFactory sagaInstanceFactory, final BookTripSaga bookTripSaga) {
        this.tripInformationRepository = tripInformationRepository;
        this.sagaInstanceFactory = sagaInstanceFactory;
        this.bookTripSaga = bookTripSaga;
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
    public TripInformation getTripInformation(Long tripId) throws TravelException {
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
    public TripInformation bookTrip(TripInformation tripInformation) {
        logger.info("Saving the booked Trip: " + tripInformation);

        tripInformationRepository.save(tripInformation);

        // Create the BookTripSaga with the necessary information
        BookTripSagaData sagaData = new BookTripSagaData(tripInformation.getId(), tripInformation);
        sagaInstanceFactory.create(bookTripSaga, sagaData);

        return tripInformation;
    }

    @Override
    public boolean cancelTrip(Long tripId) throws TravelException {
        logger.info("Cancelling the booked trip with ID " + tripId);

        TripInformation tripInformation = getTripInformation(tripId);

        if (tripInformation == null) {
            String message = String.format("The trip booking (ID: %d) could not be updated.", tripId);
            logger.info(message);
            throw new TravelException(ErrorType.INTERNAL_ERROR, message);
        }

        tripInformation.cancel(BookingStatus.CANCELLED);
        tripInformationRepository.save(tripInformation);

        return true;
    }

    @Override
    public void rejectTrip(Long tripId, RejectionReason rejectionReason) throws TravelException {
        logger.info("Rejecting the booked trip with ID " + tripId);

        TripInformation tripInformation = getTripInformation(tripId);

        if (tripInformation == null) {
            String message = String.format("The trip booking (ID: %d) could not be rejected.", tripId);
            logger.info(message);
            throw new TravelException(ErrorType.INTERNAL_ERROR, message);
        }


        BookingStatus newBookingStatus = convertToBookingStatus(rejectionReason);
        tripInformation.reject(newBookingStatus);
        tripInformationRepository.save(tripInformation);
    }

    @Override
    public void confirmTripBooking(Long tripId) throws TravelException {
        logger.info("Confirming the booked trip with ID " + tripId);

        TripInformation tripInformation = getTripInformation(tripId);

        if (tripInformation == null) {
            String message = String.format("The trip booking (ID: %d) could not be confirmed.", tripId);
            logger.info(message);
            throw new TravelException(ErrorType.INTERNAL_ERROR, message);
        }

        tripInformation.confirm(BookingStatus.CONFIRMED);
        tripInformationRepository.save(tripInformation);
    }

    private BookingStatus convertToBookingStatus(RejectionReason rejectionReason) {
        switch(rejectionReason) {
            case NO_HOTEL_AVAILABLE:
                return BookingStatus.REJECTED_NO_HOTEL_AVAILABLE;
            case NO_FLIGHT_AVAILABLE:
                return BookingStatus.REJECTED_NO_FLIGHT_AVAILABLE;
            default:
                return BookingStatus.REJECTED_UNKNOWN;
        }
    }
}
