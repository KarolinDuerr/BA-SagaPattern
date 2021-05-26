package saga.eventuate.tram.flightservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.*;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightServiceException;
import saga.eventuate.tram.flightservice.model.BookingStatus;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.resources.DtoConverter;

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
                .onMessage(CancelFlightBooking.class, this::cancelFlightBooking)
                .onMessage(CancelFlightRequest.class, this::cancelFlight)
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
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());

            if (exception.getErrorType() == ErrorType.NO_FLIGHT_AVAILABLE) {
                return CommandHandlerReplyBuilder.withFailure(new NoFlightAvailable(bookFlightCommand.getTripId()));
            }

            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message cancelFlightBooking(CommandMessage<CancelFlightBooking> command) {
        CancelFlightBooking cancelFlightBooking = command.getCommand();
        logger.info("Received CancelHotelBooking: " + cancelFlightBooking);

        flightService.cancelFlightBooking(cancelFlightBooking.getBookingId(), cancelFlightBooking.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message cancelFlight(CommandMessage<CancelFlightRequest> command) {
        CancelFlightRequest cancelFlightRequest = command.getCommand();
        logger.info("Received CancelFlightRequest: " + cancelFlightRequest);

        try {
            flightService.cancelFlight(cancelFlightRequest.getBookingId(), cancelFlightRequest.getTripId());

            CancelFlightResponse cancelFlightResponse = new CancelFlightResponse(cancelFlightRequest.getTripId(),
                    BookingStatus.CANCELLED.toString());
            return CommandHandlerReplyBuilder.withSuccess(cancelFlightResponse);
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());

            if (exception.getErrorType() == ErrorType.CANCELLATION_NON_ALLOWED) {
                return CommandHandlerReplyBuilder.withFailure(new NonAllowedFlightCancellation(cancelFlightRequest.getBookingId(), cancelFlightRequest.getTripId()));
            }

            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }
}
