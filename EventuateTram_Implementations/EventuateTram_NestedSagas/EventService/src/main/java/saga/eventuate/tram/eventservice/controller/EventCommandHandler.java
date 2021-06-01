package saga.eventuate.tram.eventservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.eventuate.tram.eventservice.api.EventServiceChannels;
import saga.eventuate.tram.eventservice.api.dto.*;
import saga.eventuate.tram.eventservice.error.ErrorType;
import saga.eventuate.tram.eventservice.error.EventServiceException;
import saga.eventuate.tram.eventservice.model.EventBooking;

/**
 * The event service Saga Participant for handling the commands created by the BookHotelEventSaga.
 */
public class EventCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventCommandHandler.class);

    @Autowired
    private final IEventService eventService;

    public EventCommandHandler(final IEventService eventService) {
        this.eventService = eventService;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(EventServiceChannels.eventServiceChannel)
                .onMessage(BookEventRequest.class, this::bookEvent)
                .onMessage(CancelEventBooking.class, this::cancelEvent)
                .onMessage(ConfirmEventBooking.class, this::confirmEvent)
                .build();
    }

    private Message bookEvent(CommandMessage<BookEventRequest> command) {
        final BookEventRequest bookEventRequest = command.getCommand();
        logger.info("Received BookEventRequest: " + bookEventRequest);

        try {
            EventBooking eventBooking = eventService.bookEvent(bookEventRequest.getTravellerName(),
                    bookEventRequest.getEventId(), bookEventRequest.getHotelBookingId());

            BookEventResponse bookingResponse = new BookEventResponse(eventBooking.getBookedEvent().getEventName(),
                    eventBooking.getId(), eventBooking.getBookingStatus().toString());
            return CommandHandlerReplyBuilder.withSuccess(bookingResponse);
        } catch (EventServiceException exception) {
            logger.error(exception.toString());
            if (exception.getErrorType() == ErrorType.NO_AVAILABLE_SPACE) {
                return CommandHandlerReplyBuilder.withFailure(new NoEventSpaceAvailable(bookEventRequest.getHotelBookingId()));
            }
            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message cancelEvent(CommandMessage<CancelEventBooking> command) {
        CancelEventBooking cancelEventBooking = command.getCommand();
        logger.info("Received CancelEventBooking: " + cancelEventBooking);

        eventService.cancelEventBooking(cancelEventBooking.getBookingId(), cancelEventBooking.getHotelBookingId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmEvent(CommandMessage<ConfirmEventBooking> command) {
        ConfirmEventBooking confirmEventBooking = command.getCommand();
        logger.info("Received ConfirmEventBooking: " + confirmEventBooking);

        eventService.confirmEventBooking(confirmEventBooking.getEventBookingId(), confirmEventBooking.getHotelBookingId());
        return CommandHandlerReplyBuilder.withSuccess();
    }
}
