package saga.eventuate.tram.hotelservice.saga;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saga.eventuate.tram.eventservice.api.EventServiceChannels;
import saga.eventuate.tram.eventservice.api.dto.BookEventResponse;
import saga.eventuate.tram.eventservice.api.dto.ConfirmEventBooking;
import saga.eventuate.tram.eventservice.api.dto.NoEventSpaceAvailable;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.CancelHotelBooking;
import saga.eventuate.tram.hotelservice.command.ConfirmHotelEventBooking;
import saga.eventuate.tram.hotelservice.command.RejectHotelEventCommand;
import saga.eventuate.tram.hotelservice.model.RejectionReason;

/**
 * The defined Saga Orchestrator for booking a trip.
 */
public class BookHotelEventSaga implements SimpleSaga<BookHotelEventSagaData> {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelEventSaga.class);

    private final SagaDefinition<BookHotelEventSagaData> sagaDefinition;

    public BookHotelEventSaga() {
        this.sagaDefinition =
                step()
                        .withCompensation(this::rejectBooking)
                        .step()
                        .invokeParticipant(this::bookEvent)
                        .onReply(BookEventResponse.class, this::handleBookEventReply)
                        .onReply(NoEventSpaceAvailable.class, this::handleNoEventSpaceAvailableReply)
                        .withCompensation(this::cancelEvent)
//                        .step()
//                        .invokeParticipant(this::bookFlight)
//                        .onReply(BookFlightResponse.class, this::handleBookFlightReply)
//                        .onReply(NoFlightAvailable.class, this::handleNoFlightAvailableReply)
//                        .withCompensation(this::cancelFlight)
                        .step()
                        .invokeParticipant(this::confirmEvent)
                        .step()
                        .invokeParticipant(this::confirmHotelEvent)
                        .build();
    }

    @Override
    public SagaDefinition<BookHotelEventSagaData> getSagaDefinition() {
        return sagaDefinition;
    }

    private CommandWithDestination rejectBooking(BookHotelEventSagaData hotelEventSagaData) {
        logger.info("Rejecting the trip booking.");

        return CommandWithDestinationBuilder.send(new RejectHotelEventCommand(hotelEventSagaData.getHotelBookingId(),
                hotelEventSagaData.getRejectionReason()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private CommandWithDestination bookEvent(BookHotelEventSagaData hotelEventSagaData) {
        logger.info("Trying to book an event.");

        return CommandWithDestinationBuilder.send(hotelEventSagaData.makeBookEventRequest())
                .to(EventServiceChannels.eventServiceChannel)
                .build();
    }

    private void handleBookEventReply(BookHotelEventSagaData hotelEventSagaData, BookEventResponse bookEventResponse) {
        logger.info("Event has been successfully booked: " + bookEventResponse.getEventName());

        hotelEventSagaData.setEventBookingId(bookEventResponse.getEventBookingId());
    }

    private void handleNoEventSpaceAvailableReply(BookHotelEventSagaData hotelEventSagaData,
                                                  NoEventSpaceAvailable noEventSpaceAvailable) {
        logger.debug("Received provoked NoEventSpaceAvailable response for hotel booking " + noEventSpaceAvailable.getHotelBookingId());
        hotelEventSagaData.setRejectionReason(RejectionReason.NO_EVENT_SPACE_AVAILABLE);
    }

    private CommandWithDestination cancelEvent(BookHotelEventSagaData hotelEventSagaData) {
        logger.info("Compensating event booking --> cancel");

        return CommandWithDestinationBuilder.send(new CancelHotelBooking(hotelEventSagaData.getHotelBookingId(),
                hotelEventSagaData.getEventId()))
                .to(EventServiceChannels.eventServiceChannel)
                .build();
    }

//    private CommandWithDestination bookFlight(BookHotelEventSagaData hotelEventSagaData) {
//        logger.info("Trying to book a flight.");
//
//        return CommandWithDestinationBuilder.send(hotelEventSagaData.makeBookFlightCommand())
//                .to(FlightServiceChannels.flightServiceChannel)
//                .build();
//    }
//
//    private void handleBookFlightReply(BookHotelEventSagaData hotelEventSagaData, BookFlightResponse
//    bookFlightResponse) {
//        logger.info("Flight has been successfully booked: " + bookFlightResponse.getFlightBookingId());
//
//        hotelEventSagaData.setFlightId(bookFlightResponse.getFlightBookingId());
//    }
//
//    private void handleNoFlightAvailableReply(BookHotelEventSagaData hotelEventSagaData, NoFlightAvailable
//    noFlightAvailable) {
//        logger.debug("Received provoked NoFlightAvailable response for trip " + noFlightAvailable.getTripId());
//        hotelEventSagaData.setRejectionReason(RejectionReason.NO_FLIGHT_AVAILABLE);
//    }

//    private CommandWithDestination cancelFlight(BookHotelEventSagaData hotelEventSagaData) {
//        logger.info("Compensating flight booking --> cancel");
//
//        return CommandWithDestinationBuilder.send(new CancelFlightBooking(hotelEventSagaData.getFlightId(),
//                hotelEventSagaData.getTripId()))
//                .to(FlightServiceChannels.flightServiceChannel)
//                .build();
//    }

    private CommandWithDestination confirmEvent(BookHotelEventSagaData hotelEventSagaData) {
        logger.info("Confirming the event booking: " + hotelEventSagaData.getEventId());

        return CommandWithDestinationBuilder.send(new ConfirmEventBooking(hotelEventSagaData.getEventBookingId(),
                hotelEventSagaData.getHotelBookingId()))
                .to(EventServiceChannels.eventServiceChannel)
                .build();
    }

    private CommandWithDestination confirmHotelEvent(BookHotelEventSagaData hotelEventSagaData) {
        logger.info("Confirming the event booking: " + hotelEventSagaData.getEventId());

        return CommandWithDestinationBuilder.send(new ConfirmHotelEventBooking(hotelEventSagaData.getHotelBookingId(),
                hotelEventSagaData.getEventBookingId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }
}
