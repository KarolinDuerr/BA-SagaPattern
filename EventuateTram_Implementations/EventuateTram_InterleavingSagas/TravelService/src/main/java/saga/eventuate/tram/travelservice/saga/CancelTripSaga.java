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
import saga.eventuate.tram.hotelservice.api.dto.NonAllowedHotelCancellation;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripCancellationCommand;
import saga.eventuate.tram.travelservice.command.RejectTripCancellationCommand;
import saga.eventuate.tram.travelservice.model.RejectionReason;

/**
 * The defined Saga Orchestrator for cancelling a trip.
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
                        .onReply(NonAllowedHotelCancellation.class, this::handleNonAllowedCancellationReply)
                        .withCompensation(this::rebookHotel)
                        .step()
                        .invokeParticipant(this::cancelFlight)
                        .onReply(CancelFlightResponse.class, this::handleCancelFlightReply)
                        .onReply(NonAllowedFlightCancellation.class, this::handleNonAllowedCancellationReply)
                        .withCompensation(this::rebookFlight)
                        .step()
                        .invokeParticipant(this::confirmHotelCancellation)
                        .step()
                        .invokeParticipant(this::confirmTripCancellation)
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

        return CommandWithDestinationBuilder.send(new CancelHotelRequest(cancelTripSagaData.getHotelId(),
                cancelTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private void handleCancelHotelReply(CancelTripSagaData cancelTripSagaData,
                                        CancelHotelResponse cancelHotelResponse) {
        logger.info("Hotel has been successfully cancelled: " + cancelHotelResponse.getBookingId());
    }

    private void handleNonAllowedCancellationReply(CancelTripSagaData cancelTripSagaData,
                                                   NonAllowedHotelCancellation nonAllowedCancellation) {
        logger.debug("Received provoked NonAllowedCancellation response for trip " + nonAllowedCancellation.getTripId());
        cancelTripSagaData.setRejectionReason(RejectionReason.HOTEL_CANCELLATION_REJECTED);
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

        return CommandWithDestinationBuilder.send(new CancelFlightRequest(cancelTripSagaData.getFlightId(),
                cancelTripSagaData.getTripId()))
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

    private void handleCancelFlightReply(CancelTripSagaData cancelTripSagaData,
                                         CancelFlightResponse cancelFlightResponse) {
        logger.info("Flight has been successfully cancelled: " + cancelFlightResponse.getFlightBookingId());
    }

    private void handleNonAllowedCancellationReply(CancelTripSagaData cancelTripSagaData,
                                                   NonAllowedFlightCancellation nonAllowedCancellation) {
        logger.debug("Received provoked NonAllowedFlightCancellation response for trip " + nonAllowedCancellation.getTripId());
        cancelTripSagaData.setRejectionReason(RejectionReason.FLIGHT_CANCELLATION_REJECTED);
    }

    private CommandWithDestination rebookFlight(CancelTripSagaData cancelTripSagaData) {
        logger.info("Compensating flight cancellation --> rebooking");

        return CommandWithDestinationBuilder.send(new RebookFlightRequest(cancelTripSagaData.getFlightId(),
                cancelTripSagaData.getTripId()))
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

    private CommandWithDestination confirmHotelCancellation(CancelTripSagaData cancelTripSagaData) {
        logger.info("Confirming the hotel cancellation: " + cancelTripSagaData.getHotelId());

        return CommandWithDestinationBuilder.send(new ConfirmHotelCancellation(cancelTripSagaData.getHotelId(),
                cancelTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private CommandWithDestination confirmTripCancellation(CancelTripSagaData cancelTripSagaData) {
        logger.info("Confirming the trip cancellation: " + cancelTripSagaData.getTripId());

        return CommandWithDestinationBuilder.send(new ConfirmTripCancellationCommand(cancelTripSagaData.getTripId(),
                cancelTripSagaData.getHotelId(), cancelTripSagaData.getFlightId()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }
}
