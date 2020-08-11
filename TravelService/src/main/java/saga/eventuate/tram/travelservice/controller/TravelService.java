package saga.eventuate.tram.travelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.travelservice.error.ErrorType;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.BookingStatus;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.model.TripInformationRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("TravelService")
public class TravelService implements  ITravelService {

    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);

    @Autowired
    private TripInformationRepository tripInformationRepository;

    public TravelService(TripInformationRepository tripInformationRepository) {
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

        tripInformation.setBookingStatus(BookingStatus.CANCELLED);
        tripInformationRepository.save(tripInformation);

        return true;
    }
}
