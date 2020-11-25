package saga.eventuate.tram.flightservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
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

public class FlightCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(FlightCommandHandler.class);

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    private final String travelServiceServer;
    private final String travelServiceActuatorUri;

    public FlightCommandHandler(final IFlightService flightService, final DtoConverter dtoConverter,
                                String travelServiceServer, String travelServiceActuatorUri) {
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
        this.travelServiceServer = travelServiceServer;
        this.travelServiceActuatorUri = travelServiceActuatorUri;
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

            // provoke Orchestrator failure
            provokeOrchestratorFailure(flightInformation.getDestination().getCountry());

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

    private void provokeOrchestratorFailure(String failureInput) {
        if (!failureInput.equalsIgnoreCase("Provoke orchestrator failure while executing")) {
            return;
        }

        logger.info("Shutting down TravelService due to corresponding input.");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> terminateRequest = new HttpEntity<>(null, headers);
        String response = restTemplate.postForObject(travelServiceServer + travelServiceActuatorUri,
                terminateRequest, String.class);
        logger.info("TravelService response" + response);
        try {
            // wait to make sure no unintended behaviour is created due to the provoked shutdown
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
