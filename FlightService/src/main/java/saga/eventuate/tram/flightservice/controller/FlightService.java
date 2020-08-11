package saga.eventuate.tram.flightservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.BookingStatus;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformationRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("FlightService")
public class FlightService implements IFlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private final FlightInformationRepository flightInformationRepository;

    public FlightService(FlightInformationRepository flightInformationRepository) {
        this.flightInformationRepository = flightInformationRepository;
    }

    @Override
    public List<FlightInformation> getFlightsInformation() {
        logger.info("Get flight bookings from repository.");

        List<FlightInformation> flightsInformation = new LinkedList<>();

        Iterable<FlightInformation> information = flightInformationRepository.findAll();

        for (FlightInformation flightInformation : information) {
            flightsInformation.add(flightInformation);
        }

        return flightsInformation;
    }

    @Override
    public FlightInformation getFlightInformation(Long flightBookingId) throws FlightException {
        logger.info(String.format("Get flight information (ID: %d) from Repository.", flightBookingId));

        Optional<FlightInformation> flightInformation = flightInformationRepository.findById(flightBookingId);

        if (!flightInformation.isPresent()) {
            String message = String.format("The flight information (ID: %d) does not exist.", flightBookingId);
            logger.info(message);
            throw new FlightException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return flightInformation.get();
    }

    @Override
    public FlightInformation bookFlight(FlightInformation flightInformation) {
        logger.info("Saving the flight information: " + flightInformation);

        flightInformationRepository.save(flightInformation);

        return flightInformation;
    }

    @Override
    public FlightInformation bookFlight(FlightInformation flightInformation, int tripId) {
        logger.info(String.format("Saving the flight information: %s, with tripId= %d", flightInformation, tripId));

        flightInformation.setTripId(tripId);
        flightInformationRepository.save(flightInformation);

        return flightInformation;
    }

    @Override
    public boolean cancelFlightBooking(Long flightBookingId) throws FlightException {
        logger.info("Cancelling the booked flight with ID " + flightBookingId);

        FlightInformation flightInformation = getFlightInformation(flightBookingId);

        if (flightInformation == null) {
            String message = String.format("The flight booking (ID: %d) could not be updated.", flightBookingId);
            logger.info(message);
            throw new FlightException(ErrorType.INTERNAL_ERROR, message);
        }

        flightInformation.setBookingStatus(BookingStatus.CANCELLED);
        flightInformationRepository.save(flightInformation);

        return true;
    }
}
