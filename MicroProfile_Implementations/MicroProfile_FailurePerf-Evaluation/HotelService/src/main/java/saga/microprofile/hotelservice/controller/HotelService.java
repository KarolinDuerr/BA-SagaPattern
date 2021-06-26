package saga.microprofile.hotelservice.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import saga.microprofile.hotelservice.error.BookingNotFound;
import saga.microprofile.hotelservice.error.ErrorType;
import saga.microprofile.hotelservice.error.HotelException;
import saga.microprofile.hotelservice.model.*;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

@ApplicationScoped
@HotelServiceImpl
public class HotelService implements IHotelService {

    private static final Logger logger = Logger.getLogger(HotelService.class.toString());

    @Resource(lookup = "concurrent/threadFactory2")
    private ThreadFactory threadFactory;

    @Inject
    @ConfigProperty(name = "lra.coordinator.uri", defaultValue = "http://localhost:8090/lrac/lra-coordinator")
    private String lraCoordinatorUri;

    @Inject
    private HotelBookingRepository hotelBookingRepository;

    public HotelService() {
    }

    @Override
    public List<HotelBooking> getHotelBookings() {
        logger.info("Get hotel bookings from Repository.");

        List<HotelBooking> hotelBookings = new LinkedList<>();

        Iterable<HotelBooking> bookings = hotelBookingRepository.findAll();

        for (HotelBooking booking : bookings) {
            hotelBookings.add(booking);
        }

        return hotelBookings;
    }

    @Override
    public HotelBooking getHotelBooking(final Long bookingId) throws HotelException {
        logger.info(String.format("Get hotel booking (ID: %d) from Repository.", bookingId));

        HotelBooking hotelBooking = hotelBookingRepository.findById(bookingId);

        if (hotelBooking == null) {
            String message = String.format("The hotel booking (ID: %d) does not exist.", bookingId);
            logger.info(message);
            throw new HotelException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return hotelBooking;
    }

    @Override
    public HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException {
        logger.info("Saving the booked Hotel: " + hotelBooking);

        HotelBooking newHotelBooking = findAvailableHotel(travellerName, hotelBooking);

        // no trip assigned therefore the booking has already been confirmed
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getTripId() == -1) {
            newHotelBooking.confirm();
        }

        // ensure idempotence of hotel bookings
        HotelBooking alreadyExistingHotelBooking = checkIfBookingAlreadyExists(newHotelBooking);
        if (alreadyExistingHotelBooking != null) {
            return alreadyExistingHotelBooking;
        }

        hotelBookingRepository.save(newHotelBooking);

        // provoke different failure scenarios
        provokeFailures(hotelBooking.getDestination().getCountry(), hotelBooking.getLraId());

        return newHotelBooking;
    }

    @Override
    public void cancelHotelBooking(final URI lraId) {
        logger.info("Cancelling the booked hotel associated with LRA ID " + lraId);

        try {
            HotelBooking hotelBooking = findBookingByLraId(lraId);


            if (hotelBooking == null) {
                logger.info(String.format("No hotel has been booked for this trip (LRA ID: %s) yet, therefore no " +
                        "need to cancel.", lraId));
                // no hotel has been booked for this trip yet, therefore no need to cancel
                return;
            }

            hotelBooking.cancel(BookingStatus.CANCELLED);
            hotelBookingRepository.update(hotelBooking);
        } catch (HotelException hotelException) {
            logger.info("Exception: " + hotelException.getMessage());
            logger.info(String.format("No hotel has been booked for this trip (LRA ID: %s) yet, therefore no " +
                    "need to cancel.", lraId));
            // no flight has been booked for this trip yet, therefore no need to cancel
        }
    }

    @Override
    public void confirmHotelBooking(final Long bookingId, final Long tripId) {
        logger.info("Confirming the booked hotel with ID " + bookingId);

        try {
            HotelBooking hotelBooking = getHotelBooking(bookingId);

            if (hotelBooking.getBookingInformation() == null || hotelBooking.getBookingInformation().getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            hotelBooking.confirm();
            hotelBookingRepository.update(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    @Override
    public void provokeOldMessageToOrchestrator(final URI lraId) {
        try {
            HotelBooking hotelBooking = findBookingByLraId(lraId);

            if (hotelBooking == null) {
                throw new BookingNotFound(lraId);
            }

            provokeOldMessageToOrchestrator(hotelBooking.getBookingInformation().getDestination().getCountry(), lraId);

        } catch (HotelException hotelException) {
            throw new BookingNotFound(lraId);
        }
    }

    // only mocking the general function of this method
    private HotelBooking findAvailableHotel(final String travellerName,
                                            final HotelBookingInformation hotelBookingInformation) throws HotelException {
        if (hotelBookingInformation.getDestination().getCountry().equalsIgnoreCase("Provoke hotel failure")) {
            logger.info("Provoked hotel exception: no available hotel for trip: " + hotelBookingInformation.getTripId());
            throw new HotelException(ErrorType.NO_HOTEL_AVAILABLE, "No available hotel found.");
        }

        return new HotelBooking("Example_Hotel", travellerName, hotelBookingInformation);
    }

    // ensure idempotence of hotel bookings
    private HotelBooking checkIfBookingAlreadyExists(final HotelBooking hotelBooking) {
        List<HotelBooking> customerHotelBookings =
                hotelBookingRepository.findByTravellerName(hotelBooking.getTravellerName());

        Optional<HotelBooking> savedHotelBooking =
                customerHotelBookings.stream().filter(hotelInfo -> hotelInfo.equals(hotelBooking)).findFirst();

        if (!savedHotelBooking.isPresent()) {
            return null;
        }

        logger.info("Hotel has already been booked: " + savedHotelBooking);
        return savedHotelBooking.get();
    }

    // enable compensation of bookings related with a LRA
    private HotelBooking findBookingByLraId(final URI lraId) throws HotelException {
        List<HotelBooking> customerHotelBookings =
                hotelBookingRepository.findByLraId(lraId.toString());

        Optional<HotelBooking> savedHotelBooking = customerHotelBookings.stream().findFirst();

        if (!savedHotelBooking.isPresent()) {
            throw new HotelException(ErrorType.NON_EXISTING_ITEM, "Related trip could not be found");
        }

        logger.info("Related hotel booking has been found: " + savedHotelBooking);
        return savedHotelBooking.get();
    }

    private void provokeFailures(final String failureInput, final String lraId) {
        // provoke Participant failure (FlightService)
        provokeParticipantFailure(failureInput);

        // provoke duplicate message --> acknowledge task twice // TODO
        provokeDuplicateMessageToOrchestrator(failureInput, lraId);
    }

    private void provokeParticipantFailure(final String failureInput) {
        if (!failureInput.equalsIgnoreCase("Provoke participant failure before receiving task")) {
            return;
        }

        logger.info("Shutting down FlightService due to corresponding input.");

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
        dockerClient.stopContainerCmd("flightservice_microProfileFailurePerf").exec();

        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warning("Docker client could not be closed. \nException: " + e.getMessage());
        }
    }

    private void provokeDuplicateMessageToOrchestrator(final String failureInput, final String lraId) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate message to orchestrator")) {
            return;
        }

        logger.info("Provoking immediate duplicate message to orchestrator.");

        // TODO check if provoking duplicate messages is possible
//        String updateTaskEndpoint = "/tasks";
//        HttpHeaders requestHeader = new HttpHeaders();
//        requestHeader.setContentType(MediaType.APPLICATION_JSON);
//        taskResult.setStatus(TaskResult.Status.COMPLETED);
//        final JsonNode result =
//                objectMapper.valueToTree(taskResult);
//
//        HttpEntity<JsonNode> request = new HttpEntity<>(result, requestHeader);
//
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            String taskId = restTemplate.postForObject(conductorServerUri + updateTaskEndpoint, request, String
//            .class);
//            logger.info("Received taskId: " + taskId);
//        } catch (RestClientException restClientException) {
//            logger.info("Received exception: " + restClientException.getMessage());
//        }
    }

    private void provokeOldMessageToOrchestrator(final String failureInput, final URI lraId) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old message to orchestrator")) {
            return;
        }

        logger.info("Provoking delayed close LRA message to orchestrator --> old message");
        threadFactory.newThread(new OldMessageProvoker(lraCoordinatorUri, lraId)).start();
    }
}
