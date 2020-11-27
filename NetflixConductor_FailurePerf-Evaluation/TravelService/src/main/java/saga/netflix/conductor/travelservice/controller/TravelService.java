package saga.netflix.conductor.travelservice.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.netflix.conductor.client.exceptions.ConductorClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saga.netflix.conductor.travelservice.error.BookingNotFound;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netflix.conductor.travelservice.error.TravelException;
import saga.netflix.conductor.travelservice.model.BookingStatus;
import saga.netflix.conductor.travelservice.model.RejectionReason;
import saga.netflix.conductor.travelservice.model.TripInformation;
import saga.netflix.conductor.travelservice.model.TripInformationRepository;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;

import java.io.IOException;
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
    public TripInformation bookTrip(final TripInformation tripInformation) throws TravelException {
        logger.info("Saving the booked Trip: " + tripInformation);

        // ensure idempotence of trip bookings
        TripInformation alreadyExistingTripBooking = checkIfBookingAlreadyExists(tripInformation);
        if (alreadyExistingTripBooking != null) {
            return alreadyExistingTripBooking;
        }

        tripInformationRepository.save(tripInformation);

        // provoke Orchestrator failure before the Saga is being started if the appropriate input is given
        provokeOrchestratorFailure(tripInformation.getDestination().getCountry());

        // create and start the BookTripSaga with necessary information
        BookTripSagaData bookTripSagaData = new BookTripSagaData(tripInformation.getId(), tripInformation);
        try {
            sagaInstanceFactory.startBookTripSaga(bookTripSagaData);
        }  catch (ConductorClientException networkException) {
            tripInformationRepository.deleteById(tripInformation.getId());
            logger.error("Saga could not be started: " + networkException.getMessage());
            throw new TravelException(ErrorType.INTERNAL_ERROR, "Connection to server was not possible. Please try " +
                    "again later.");
        }

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

    private void provokeOrchestratorFailure(String failureInput) {
        if (!failureInput.equalsIgnoreCase("Provoke orchestrator failure while starting trip booking")) {
            return;
        }

        logger.info("Shutting down ConductorServer due to corresponding input.");
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
        dockerClient.stopContainerCmd("conductor-server-ui").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }
}
