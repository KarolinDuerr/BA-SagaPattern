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
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelServiceException;
import saga.eventuate.tram.hotelservice.model.BookingStatus;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.resources.DtoConverter;

/**
 * The hotel service Saga Participant for handling the commands created by the BookTripSaga and the CancelTripSaga.
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
                .onMessage(CancelHotelBooking.class, this::cancelHotelBooking)
                .onMessage(ConfirmHotelBooking.class, this::confirmHotel)
                .onMessage(CancelHotelRequest.class, this::cancelHotel)
                .onMessage(ConfirmHotelCancellation.class, this::confirmHotelCancellation)
                .onMessage(RebookHotelRequest.class, this::rebookHotel)
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
            return CommandHandlerReplyBuilder.withSuccess(bookingResponse);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            if (exception.getErrorType() == ErrorType.NO_AVAILABLE_HOTEL) {
                return CommandHandlerReplyBuilder.withFailure(new NoHotelAvailable(bookHotelRequest.getTripId()));
            }
            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message cancelHotelBooking(CommandMessage<CancelHotelBooking> command) {
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

    private Message cancelHotel(CommandMessage<CancelHotelRequest> command) {
        CancelHotelRequest cancelHotelRequest = command.getCommand();
        logger.info("Received CancelHotelRequest: " + cancelHotelRequest);

        try {
            hotelService.cancelHotel(cancelHotelRequest.getBookingId(), cancelHotelRequest.getTripId());
            CancelHotelResponse cancelHotelResponse = new CancelHotelResponse(cancelHotelRequest.getTripId(),
                    BookingStatus.CANCELLING.toString());
            return CommandHandlerReplyBuilder.withSuccess(cancelHotelResponse);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            if (exception.getErrorType() == ErrorType.CANCELLATION_NON_ALLOWED) {
                return CommandHandlerReplyBuilder.withFailure(
                        new NonAllowedHotelCancellation(cancelHotelRequest.getBookingId(), cancelHotelRequest.getTripId()));
            }
            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }

    private Message confirmHotelCancellation(CommandMessage<ConfirmHotelCancellation> command) {
        ConfirmHotelCancellation confirmHotelCancellation = command.getCommand();
        logger.info("Received ConfirmHotelBooking: " + confirmHotelCancellation);

        hotelService.confirmHotelCancellation(confirmHotelCancellation.getBookingId(),
                confirmHotelCancellation.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message rebookHotel(CommandMessage<RebookHotelRequest> command) {
        final RebookHotelRequest rebookHotelRequest = command.getCommand();
        logger.info("Received RebookHotelRequest: " + rebookHotelRequest);

        hotelService.rebookHotel(rebookHotelRequest.getBookingId(), rebookHotelRequest.getTripId());

        return CommandHandlerReplyBuilder.withSuccess();
    }
}
