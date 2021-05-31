package saga.eventuate.tram.flightservice.controller;

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
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.BookFlightResponse;
import saga.eventuate.tram.flightservice.api.dto.CancelFlightBooking;
import saga.eventuate.tram.flightservice.api.dto.NoFlightAvailable;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightServiceException;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.resources.DtoConverter;

import java.io.IOException;

public class FlightCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(FlightCommandHandler.class);

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    public FlightCommandHandler(final IFlightService flightService, final DtoConverter dtoConverter) {
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(FlightServiceChannels.flightServiceChannel)
                .onMessage(BookFlightCommand.class, this::bookFlight)
                .onMessage(CancelFlightBooking.class, this::cancelFlight)
                .build();
    }

    private Message bookFlight(CommandMessage<BookFlightCommand> command) {
        final BookFlightCommand bookFlightCommand = command.getCommand();
        logger.info("Received BookFlightCommand: " + bookFlightCommand);

        try {
            FindAndBookFlightInformation flightInformation =
                    dtoConverter.convertToFindAndBookFlightInformation(bookFlightCommand);
            FlightInformation receivedFlightInformation = flightService.findAndBookFlight(flightInformation);

            BookFlightResponse bookFlightResponse = new BookFlightResponse(receivedFlightInformation.getId(),
                    receivedFlightInformation.getBookingStatus().toString());

            // provoke Orchestrator failure --> CDC service or TravelService
            provokeOrchestratorFailure(flightInformation.getDestination().getCountry(), receivedFlightInformation.getProvokedFailure());

            // provoke own failure if it has not already been done before for this flight booking
            provokeOwnFailure(flightInformation.getDestination().getCountry(),
                    receivedFlightInformation.getProvokedFailure());

            return CommandHandlerReplyBuilder.withSuccess(bookFlightResponse);
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());

            if (exception.getErrorType() == ErrorType.NO_FLIGHT_AVAILABLE) {
                return CommandHandlerReplyBuilder.withFailure(new NoFlightAvailable(bookFlightCommand.getTripId()));
            }

            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message cancelFlight(CommandMessage<CancelFlightBooking> command) {
        CancelFlightBooking cancelFlightBooking = command.getCommand();
        logger.info("Received CancelHotelBooking: " + cancelFlightBooking);

        flightService.cancelFlightBooking(cancelFlightBooking.getBookingId(), cancelFlightBooking.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private void provokeOrchestratorFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (alreadyBeenProvoked) {
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke orchestrator failure while executing")) {
            logger.info("Shutting down TravelService due to corresponding input.");
            provokeServiceFailure("travelservice_eventuateFailurePerf");
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke CDC failure while executing")) {
            logger.info("Shutting down CDC service due to corresponding input.");
            provokeServiceFailure("cdcservice");
        }
    }

    private void provokeOwnFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (alreadyBeenProvoked) {
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke participant failure while executing")) {
            logger.info("Shutting down FlightService due to corresponding input.");
            // Force the JVM to terminate to simulate sudden failure
            Runtime.getRuntime().halt(1);
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke exception while executing")) {
            logger.info("Throwing runtime exception due to corresponding input.");
            throw new RuntimeException("Test participant behaviour when provoking exception while executing");
        }
    }

    private void provokeServiceFailure(String containerName) {
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
        dockerClient.stopContainerCmd(containerName).exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }
}
