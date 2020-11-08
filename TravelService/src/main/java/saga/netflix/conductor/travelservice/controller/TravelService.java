package saga.netflix.conductor.travelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.netflix.conductor.travelservice.error.BookingNotFound;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netflix.conductor.travelservice.error.TravelException;
import saga.netflix.conductor.travelservice.model.TripInformation;
import saga.netflix.conductor.travelservice.model.TripInformationRepository;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("TravelService")
public class TravelService implements  ITravelService {

    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);

    @Autowired
    private final TripInformationRepository tripInformationRepository;

    @Autowired
    private final SagaInstanceFactory sagaInstanceFactory;

    public TravelService(final TripInformationRepository tripInformationRepository, final SagaInstanceFactory sagaInstanceFactory) {
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

        //ensure idempotence of trip bookings
        TripInformation alreadyExistingTripBooking = checkIfBookingAlreadyExists(tripInformation);
        if (alreadyExistingTripBooking != null) {
            return alreadyExistingTripBooking;
        }

        tripInformationRepository.save(tripInformation);

        // Create and start the BookTripSaga with necessary information
        BookTripSagaData bookTripSagaData = new BookTripSagaData(tripInformation.getId(), tripInformation);
        sagaInstanceFactory.startBookTripSaga(bookTripSagaData);

        return tripInformation;
    }

    @Override
    public void confirmTripBooking(final Long tripId, final long hotelId, final long flightId) {
        logger.info("Confirming the booked trip with ID " + tripId);

        TripInformation tripInformation = null;
        try {
            tripInformation = getTripInformation(tripId);

            tripInformation.setHotelId(hotelId);
            tripInformation.setFlightId(flightId);
            tripInformation.confirm();
            tripInformationRepository.save(tripInformation);
        } catch (TravelException exception) {
            throw new BookingNotFound(tripId);
        }
    }

    //ensure idempotence of trip bookings
    private TripInformation checkIfBookingAlreadyExists(final TripInformation tripInformation) {
        List<TripInformation> customerTrips =
                tripInformationRepository.findByCustomerId(tripInformation.getCustomerId());

        Optional<TripInformation> savedTripBooking =
                customerTrips.stream().filter(tripInfo -> tripInfo.equals(tripInformation)).findFirst();

        if (!savedTripBooking.isPresent()) {
            return null;
        }

        logger.info("Trip has already been booked: " + savedTripBooking.toString());
        return savedTripBooking.get();
    }
}
