package saga.eventuate.tram.hotelservice.controller;

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
import saga.eventuate.tram.hotelservice.command.ConfirmHotelEventBooking;
import saga.eventuate.tram.hotelservice.command.RejectHotelEventCommand;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelServiceException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.RejectionReason;
import saga.eventuate.tram.hotelservice.resources.DtoConverter;

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
                // BookTripSaga Handlers
                .onMessage(BookHotelRequest.class, this::bookHotel)
                .onMessage(CancelHotelBooking.class, this::cancelHotel)
                .onMessage(ConfirmHotelBooking.class, this::confirmHotel)
                // BookHotelEventSaga Handlers
                .onMessage(ConfirmHotelEventBooking.class, this::confirmHotelEvent)
                .onMessage(RejectHotelEventCommand.class, this::rejectHotelEvent)
                .build();
    }

    // region BookTripSaga Participant Handler methods
    private Message bookHotel(CommandMessage<BookHotelRequest> command) {
        final BookHotelRequest bookHotelRequest = command.getCommand();
        logger.info("Received BookHotelRequest: " + bookHotelRequest);

        try {
            HotelBookingInformation bookingInformation =
                    dtoConverter.convertToHotelBookingInformation(bookHotelRequest);
            HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

            BookHotelResponse bookingResponse = new BookHotelResponse(hotelBooking.getId(),
                    hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());
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

    // endregion

    // region BookEventSaga Participant Handler methods
    private Message rejectHotelEvent(CommandMessage<RejectHotelEventCommand> command) {
        final long hotelBookingId = command.getCommand().getHotelBookingId();
        final RejectionReason rejectionReason = command.getCommand().getRejectionReason();
        logger.info("Received RejectHotelEventCommand for hotelBookingId = " + hotelBookingId + ", rejection reason: " + rejectionReason);

        hotelService.rejectHotelEventBooking(hotelBookingId, rejectionReason);

        logger.info("Successfully rejected hotel event with hotelBookingId = " + hotelBookingId);
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmHotelEvent(CommandMessage<ConfirmHotelEventBooking> command) {
        final ConfirmHotelEventBooking confirmHotelEventBooking = command.getCommand();
        logger.info("Received ConfirmHotelEventBooking for hotelBookingId = " + confirmHotelEventBooking.getHotelBookingId());

        hotelService.confirmHotelEventBooking(confirmHotelEventBooking.getEventBookingId(), confirmHotelEventBooking.getHotelBookingId());

        logger.info("Successfully confirmed hotel event with hotelBookingId = " + confirmHotelEventBooking.getHotelBookingId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    // endregion
}
