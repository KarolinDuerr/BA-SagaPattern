package saga.netflix.conductor.flightservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("FlightService")
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

    @Override
    public FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException {
        logger.info("Finding a flight for the flight information: " + findAndBookFlightInformation);

        FlightInformation flightInformation = findAvailableFlight(findAndBookFlightInformation);

        //ensure idempotence of flight bookings
        FlightInformation alreadyExistingFlightInformation = checkIfBookingAlreadyExists(flightInformation);
        if (alreadyExistingFlightInformation != null) {
            return alreadyExistingFlightInformation;
        }

        flightInformationRepository.save(flightInformation);

        return flightInformation;
    }

    @Override
    public void cancelFlightBooking(final Long tripId, final String travellerName) {
        logger.info("Cancelling the booked flight associated with trip ID " + tripId);

        FlightInformation flightInformation;
        flightInformation = getTripFlightInformationByName(travellerName, tripId);

        if (flightInformation == null) {
            logger.info(String.format("No flight has been booked for this trip (ID: %s) yet, therefore no need to " +
                    "cancel.", tripId));
            // no flight has been booked for this trip yet, therefore no need to cancel
            return;
        }

        flightInformation.cancel(BookingStatus.CANCELLED);
        flightInformationRepository.save(flightInformation);
    }

    // only mocking the general function of this method
    private FlightInformation findAvailableFlight(final FindAndBookFlightInformation flightInformation) throws FlightException {
        if (flightInformation.getDestination().getCountry().equalsIgnoreCase("Provoke flight failure")) {
            logger.info("Provoked flight exception: no available flight for trip: " + flightInformation.getTripId());
            throw new FlightException(ErrorType.NO_FLIGHT_AVAILABLE, "No available flight found.");
        }

        Flight outboundFlight = new Flight(flightInformation.getHome().getCountry(),
                flightInformation.getHome().getCity(), flightInformation.getDestination().getCity(),
                flightInformation.getOutboundFlightDate());
        Flight returnFlight = new Flight(flightInformation.getDestination().getCountry(),
                flightInformation.getDestination().getCity(), flightInformation.getHome().getCity(),
                flightInformation.getReturnFlightDate());

        return new FlightInformation(outboundFlight, returnFlight, flightInformation.getTravellerName(),
                flightInformation.getTripId());
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

    private FlightInformation getTripFlightInformationByName(final String travellerName, final long tripId) {
        List<FlightInformation> customerTrips =
                flightInformationRepository.findByTravellerName(travellerName);

        Optional<FlightInformation> existingFlightInformation =
                customerTrips.stream().filter(flightInfo -> flightInfo.getTripId() == tripId).findFirst();

        // if no hotel booking can be found than no hotel has been booked for this trip yet, therefore no need to cancel
        return existingFlightInformation.orElse(null);

    }
}
