package saga.eventuate.tram.hotelservice.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.*;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelServiceException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.resources.DtoConverter;

import java.io.IOException;

/**
 * The hotel service Saga Participant for handling the commands created by the BookTripSaga.
 */
public class HotelCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(HotelCommandHandler.class);

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    public HotelCommandHandler(final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(HotelServiceChannels.hotelServiceChannel)
                .onMessage(BookHotelRequest.class, this::bookHotel)
                .onMessage(CancelHotelBooking.class, this::cancelHotel)
                .onMessage(ConfirmHotelBooking.class, this::confirmHotel)
                .build();
    }

    private Message bookHotel(CommandMessage<BookHotelRequest> command) {
        final BookHotelRequest bookHotelRequest = command.getCommand();
        logger.info("Received BookHotelRequest: " + bookHotelRequest);

        try {
            HotelBookingInformation bookingInformation =
                    dtoConverter.convertToHotelBookingInformation(bookHotelRequest);
            HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

            BookHotelResponse bookingResponse = new BookHotelResponse(hotelBooking.getId(),
                    hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

            // provoke Participant failure (FlightService)
            provokeParticipantFailure(bookingInformation.getDestination().getCountry());

            return CommandHandlerReplyBuilder.withSuccess(bookingResponse);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            if (exception.getErrorType() == ErrorType.NO_AVAILABLE_HOTEL) {
                return CommandHandlerReplyBuilder.withFailure(new NoHotelAvailable(bookHotelRequest.getTripId()));
            }
            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message cancelHotel(CommandMessage<CancelHotelBooking> command) {
        CancelHotelBooking cancelHotelBooking = command.getCommand();
        logger.info("Received CancelHotelBooking: " + cancelHotelBooking);

        hotelService.cancelHotelBooking(cancelHotelBooking.getBookingId(), cancelHotelBooking.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmHotel(CommandMessage<ConfirmHotelBooking> command) {
        ConfirmHotelBooking confirmHotelBooking = command.getCommand();
        logger.info("Received ConfirmHotelBooking: " + confirmHotelBooking);

        hotelService.confirmHotelBooking(confirmHotelBooking.getBookingId(), confirmHotelBooking.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private void provokeParticipantFailure(String failureInput) {
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
        dockerClient.stopContainerCmd("flightservice_eventuateFailurePerf").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }
}
