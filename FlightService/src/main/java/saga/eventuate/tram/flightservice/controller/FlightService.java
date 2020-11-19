package saga.eventuate.tram.flightservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.flightservice.error.BookingNotFound;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("FlightService")
@Transactional
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

        // ensure idempotence of flight bookings
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

        // ensure idempotence of flight bookings
        FlightInformation alreadyExistingFlightInformation = checkIfBookingAlreadyExists(flightInformation);
        if (alreadyExistingFlightInformation != null) {
            return alreadyExistingFlightInformation;
        }

        flightInformationRepository.save(flightInformation);

        return flightInformation;
    }

    @Override
    public boolean cancelFlightBooking(final Long flightBookingId) throws FlightException {
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

    @Override
    public void cancelFlightBooking(final Long bookingId, final Long tripId) {
        logger.info("Cancelling the booked flight with ID " + bookingId);

        try {
            FlightInformation flightInformation = getFlightInformation(bookingId);

            if (flightInformation.getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            flightInformation.cancel(BookingStatus.CANCELLED);
            flightInformationRepository.save(flightInformation);
        } catch (FlightException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    // only mocking the general function of this method
    private FlightInformation findAvailableFlight(final FindAndBookFlightInformation flightInformation) throws FlightException {
        if (flightInformation.getHome().getCountry().equalsIgnoreCase("Provoke flight failure")) {
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

    // ensure idempotence of flight bookings
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
