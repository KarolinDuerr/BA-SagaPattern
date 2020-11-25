package saga.eventuate.tram.hotelservice.controller;

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
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.*;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelServiceException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
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

    private final String flightServiceServer;
    private final String flightServiceActuatorUri;

    public HotelCommandHandler(final IHotelService hotelService, final DtoConverter dtoConverter,
                               final String flightServiceServer, final String flightServiceActuatorUri) {
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
        this.flightServiceServer = flightServiceServer;
        this.flightServiceActuatorUri = flightServiceActuatorUri;
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> terminateRequest = new HttpEntity<>(null, headers);
        String response = restTemplate.postForObject(flightServiceServer + flightServiceActuatorUri,
                terminateRequest, String.class);
        logger.info("FlightService response" + response);
        try {
            // wait to make sure no unintended behaviour is created due to the provoked shutdown
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
