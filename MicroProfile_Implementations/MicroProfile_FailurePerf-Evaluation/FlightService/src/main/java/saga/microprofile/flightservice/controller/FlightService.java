package saga.microprofile.flightservice.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.FlightException;
import saga.microprofile.flightservice.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
@FlightServiceImpl
public class FlightService implements IFlightService {

    private static final Logger logger = Logger.getLogger(FlightService.class.toString());

    @Inject
    private FlightInformationRepository flightInformationRepository;

    public FlightService() {

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

        FlightInformation flightInformation = flightInformationRepository.findById(flightBookingId);

        if (flightInformation == null) {
            String message = String.format("The flight information (ID: %d) does not exist.", flightBookingId);
            logger.info(message);
            throw new FlightException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return flightInformation;
    }

    @Override
    public FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException {
        logger.info("Finding a flight for the flight information: " + findAndBookFlightInformation);

        FlightInformation flightInformation = findAvailableFlight(findAndBookFlightInformation);

        // ensure idempotence of flight bookings
        FlightInformation alreadyExistingFlightInformation = checkIfBookingAlreadyExists(flightInformation);
        if (alreadyExistingFlightInformation != null) {
            if (alreadyExistingFlightInformation.getReturnFlight().getCountry().contains("Provoke")) {
                alreadyExistingFlightInformation.setProvokedFailure(true);
            }
            return alreadyExistingFlightInformation;
        }

        flightInformationRepository.save(flightInformation);

        // provoke different failure scenarios
        provokeFailures(findAndBookFlightInformation.getDestination().getCountry(),
                flightInformation.getProvokedFailure());

        return flightInformation;
    }

    @Override
    public void cancelFlightBooking(final URI lraId) {
        logger.info("Cancelling the booked flight associated with LRA ID " + lraId);

        try {
            FlightInformation flightInformation = findBookingByLraId(lraId);

            if (flightInformation == null) {
                logger.info(String.format("No flight has been booked for this trip (LRA ID: %s) yet, therefore no " +
                        "need to cancel.", lraId));
                // no flight has been booked for this trip yet, therefore no need to cancel
                return;
            }

            flightInformation.cancel(BookingStatus.CANCELLED);
            flightInformationRepository.update(flightInformation);
        } catch (FlightException exception) {
            logger.info("Exception: " + exception.getMessage());
            logger.info(String.format("No flight has been booked for this trip (LRA ID: %s) yet, therefore no " +
                    "need to cancel.", lraId));
            // no flight has been booked for this trip yet, therefore no need to cancel
        }
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
                flightInformation.getTripId(), flightInformation.getLraId());
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

    // enable compensation of bookings related with a LRA
    private FlightInformation findBookingByLraId(final URI lraId) throws FlightException {
        List<FlightInformation> customerFlights =
                flightInformationRepository.findByLraId(lraId.toString());

        Optional<FlightInformation> existingFlightInformation = customerFlights.stream().findFirst();

        if (!existingFlightInformation.isPresent()) {
            throw new FlightException(ErrorType.NON_EXISTING_ITEM, "Related trip could not be found");
        }

        logger.info("Related flight booking has been found: " + existingFlightInformation);
        return existingFlightInformation.get();
    }

    private void provokeFailures(final String failureInput, final boolean provokedFailure) {

        // provoke Orchestrator failure (TravelService)
        provokeOrchestratorFailure(failureInput, provokedFailure);

        // provoke own failure if it has not already been done before for this flight booking
        provokeOwnFailure(failureInput, provokedFailure);
    }

    private void provokeOrchestratorFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (!failureInput.equalsIgnoreCase("Provoke orchestrator failure while executing") || alreadyBeenProvoked) {
            return;
        }

        logger.info("Shutting down TravelService due to corresponding input.");
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .withDockerTlsVerify(false)
                .withDockerCertPath("/home/user/.docker/certs")
                .withDockerConfig("/home/user/.docker")
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        dockerClient.stopContainerCmd("travelservice_microProfileFailurePerf").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warning("Docker client could not be closed. \nException: " + e.getMessage());
        }
    }

    private void provokeOwnFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (alreadyBeenProvoked) {
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke exception while executing")) {
            logger.info("Throwing runtime exception due to corresponding input.");
            throw new RuntimeException("Test participant behaviour when provoking exception while executing");
        }

        if (failureInput.equalsIgnoreCase("Provoke participant failure while executing")) {
            logger.info("Shutting down FlightService due to corresponding input.");
            // Force the JVM to terminate to simulate sudden failure
            Runtime.getRuntime().halt(1);
        }
    }
}
