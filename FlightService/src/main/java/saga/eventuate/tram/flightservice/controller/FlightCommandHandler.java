package saga.eventuate.tram.flightservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.BookFlightResponse;
import saga.eventuate.tram.flightservice.error.ConverterException;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.resources.DtoConverter;

@Component
public class FlightCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(FlightCommandHandler.class);

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    public FlightCommandHandler(IFlightService flightService, DtoConverter dtoConverter) {
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(FlightServiceChannels.flightServiceChannel)
                .onMessage(BookFlightCommand.class, this::bookFlight)
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
            return CommandHandlerReplyBuilder.withSuccess(bookFlightResponse);
        } catch (ConverterException | FlightException exception) {
            return CommandHandlerReplyBuilder.withFailure(exception);
        }
    }
}
