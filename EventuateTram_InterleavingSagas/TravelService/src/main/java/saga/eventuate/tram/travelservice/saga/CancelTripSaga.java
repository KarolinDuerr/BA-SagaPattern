package saga.eventuate.tram.travelservice.saga;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.*;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.*;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.RejectTripCancellationCommand;
import saga.eventuate.tram.travelservice.model.RejectionReason;

/**
 * The defined Saga Orchestrator for booking a trip.
 */
public class CancelTripSaga implements SimpleSaga<CancelTripSagaData> {

    private static final Logger logger = LoggerFactory.getLogger(CancelTripSaga.class);

    private final SagaDefinition<CancelTripSagaData> sagaDefinition;

    public CancelTripSaga() {
        this.sagaDefinition =
                step()
                        .withCompensation(this::rejectCancellation)
                        .step()
                        .invokeParticipant(this::cancelHotel)
                        .onReply(CancelHotelResponse.class, this::handleCancelHotelReply)
                        .onReply(NoHotelAvailable.class, this::handleNoHotelAvailableReply) // TODO
                        .withCompensation(this::rebookHotel)
                        .step()
                        .invokeParticipant(this::cancelFlight)
                        .onReply(CancelFlightResponse.class, this::handleCancelFlightReply)
                        .onReply(NoFlightAvailable.class, this::handleNoFlightAvailableReply) // TODO
                        .withCompensation(this::rebookFlight)
                        .step()
//                        .invokeParticipant(this::confirmHotelCancellation) // TODO
//                        .step()
                        .invokeParticipant(this::confirmTripCancellation) // TODO
                        .build();
    }

    @Override
    public SagaDefinition<CancelTripSagaData> getSagaDefinition() {
        return sagaDefinition;
    }

    private CommandWithDestination rejectCancellation(CancelTripSagaData cancelTripSagaData) {
        logger.info("Rejecting the trip cancellation.");

        return CommandWithDestinationBuilder.send(new RejectTripCancellationCommand(cancelTripSagaData.getTripId(),
                cancelTripSagaData.getRejectionReason()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }

    private CommandWithDestination cancelHotel(CancelTripSagaData cancelTripSagaData) {
        logger.info("Trying to cancel the hotel.");

        return CommandWithDestinationBuilder.send(new CancelHotelBooking(cancelTripSagaData.getHotelId(),
                cancelTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private void handleCancelHotelReply(CancelTripSagaData cancelTripSagaData, CancelHotelResponse cancelHotelResponse) {
        logger.info("Hotel has been successfully cancelled: " + cancelHotelResponse.getBookingId());
    }

    private void handleNoHotelAvailableReply(CancelTripSagaData cancelTripSagaData, NoHotelAvailable noHotelAvailable) {
//        logger.debug("Received provoked NoHotelAvailable response for trip " + noHotelAvailable.getTripId());
//        bookTripSagaData.setRejectionReason(RejectionReason.NO_HOTEL_AVAILABLE);
    }

    private CommandWithDestination rebookHotel(CancelTripSagaData cancelTripSagaData) {
        logger.info("Compensating hotel cancellation --> rebooking");

        return CommandWithDestinationBuilder.send(new RebookHotelRequest(cancelTripSagaData.getHotelId(),
                cancelTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private CommandWithDestination cancelFlight(CancelTripSagaData cancelTripSagaData) {
        logger.info("Trying to cancel the flight.");

        return CommandWithDestinationBuilder.send(new CancelFlightBooking(cancelTripSagaData.getFlightId(), cancelTripSagaData.getTripId()))
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

    private void handleCancelFlightReply(CancelTripSagaData cancelTripSagaData, CancelFlightResponse cancelFlightResponse) {
        logger.info("Flight has been successfully cancelled: " + cancelFlightResponse.getFlightBookingId());
    }

    private void handleNoFlightAvailableReply(CancelTripSagaData cancelTripSagaData,
                                              NoFlightAvailable noFlightAvailable) {
//        logger.debug("Received provoked NoFlightAvailable response for trip " + noFlightAvailable.getTripId());
//        bookTripSagaData.setRejectionReason(RejectionReason.NO_FLIGHT_AVAILABLE);
    }

    private CommandWithDestination rebookFlight(CancelTripSagaData cancelTripSagaData) {
        logger.info("Compensating flight cancellation --> rebooking");

        return CommandWithDestinationBuilder.send(new RebookFlightRequest(cancelTripSagaData.getFlightId(),
                cancelTripSagaData.getTripId()))
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

//    private CommandWithDestination confirmHotelCancellation(CancelTripSagaData cancelTripSagaData) {
//        logger.info("Confirming the hotel booking: " + bookTripSagaData.getHotelId());
//
//        return CommandWithDestinationBuilder.send(new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
//                bookTripSagaData.getTripId()))
//                .to(HotelServiceChannels.hotelServiceChannel)
//                .build();
//    }

    private CommandWithDestination confirmTripCancellation(CancelTripSagaData cancelTripSagaData) {
        logger.info("Confirming the trip cancellation: " + cancelTripSagaData.getTripId());

        return CommandWithDestinationBuilder.send(new ConfirmTripBooking(cancelTripSagaData.getTripId(),
                cancelTripSagaData.getHotelId(), cancelTripSagaData.getFlightId()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }
}
