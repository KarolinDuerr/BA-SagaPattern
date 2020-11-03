package saga.netflix.conductor.flightservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.model.FlightInformation;
import saga.netflix.conductor.flightservice.model.FlightInformationRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("FlightService")
public class FlightService implements IFlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private final FlightInformationRepository flightInformationRepository;

    public FlightService(final FlightInformationRepository flightInformationRepository) {
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
    public FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException {
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
    public FlightInformation bookFlight(final FlightInformation flightInformation) {
        logger.info("Saving the flight information: " + flightInformation);

        //ensure idempotence of flight bookings
        FlightInformation alreadyExistingFlightInformation = checkIfBookingAlreadyExists(flightInformation);
        if (alreadyExistingFlightInformation != null) {
            return alreadyExistingFlightInformation;
        }

        flightInformationRepository.save(flightInformation);

        return flightInformation;
    }

    //ensure idempotence of flight bookings
    private FlightInformation checkIfBookingAlreadyExists(final FlightInformation flightInformation) {
        List<FlightInformation> customerTrips =
                flightInformationRepository.findByTravellerName(flightInformation.getTravellerName());

        Optional<FlightInformation> savedFlightInformation =
                customerTrips.stream().filter(flightInfo -> flightInfo.equals(flightInformation)).findFirst();

        if (!savedFlightInformation.isPresent()) {
            return null;
        }

        logger.info("Flight has already been booked: " + savedFlightInformation.toString());
        return savedFlightInformation.get();
    }
}
