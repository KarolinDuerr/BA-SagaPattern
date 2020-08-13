package saga.eventuate.tram.travelservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.RejectionReason;

/**
 * The travel service saga participant for handling the commands created by the BookTripSaga.
 */
@Component
public class TravelCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(TravelCommandHandler.class);

    @Autowired
    private ITravelService travelService;

    public TravelCommandHandler(ITravelService travelService) {
        this.travelService = travelService;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(TravelServiceChannels.travelServiceChannel)
                .onMessage(RejectTripCommand.class, this::rejectTrip)
                .onMessage(ConfirmTripBooking.class, this::confirmBooking)
                .build();
    }

    private Message rejectTrip(CommandMessage<RejectTripCommand> command) {
        final long tripId = command.getCommand().getTripId();
        final RejectionReason rejectionReason = command.getCommand().getRejectionReason();
        logger.info("Received RejectTripCommand for tripId = " + tripId + ", rejection reason: " + rejectionReason);

        try {
            travelService.rejectTrip(tripId, rejectionReason);
        } catch (TravelException exception) {
            return CommandHandlerReplyBuilder.withFailure(exception);
        }

        logger.info("Successfully rejected trip with tripId = " + tripId);
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmBooking(CommandMessage<ConfirmTripBooking> command) {
        final long tripId = command.getCommand().getTripId();
        logger.info("Received ConfirmTripBooking for tripId = " + tripId);

        try {
            travelService.confirmTripBooking(tripId);
        } catch (TravelException exception) {
            return CommandHandlerReplyBuilder.withFailure(exception);
        }
        logger.info("Successfully confirmed trip with tripId = " + tripId);
        return CommandHandlerReplyBuilder.withSuccess();
    }

}
