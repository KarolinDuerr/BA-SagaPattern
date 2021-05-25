package saga.eventuate.tram.travelservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.ConfirmTripCancellationCommand;
import saga.eventuate.tram.travelservice.command.RejectTripCancellationCommand;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;
import saga.eventuate.tram.travelservice.model.RejectionReason;

/**
 * The travel service Saga Participant for handling the commands created by the BookTripSaga.
 */
public class TravelCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(TravelCommandHandler.class);

    @Autowired
    private final ITravelService travelService;

    public TravelCommandHandler(final ITravelService travelService) {
        this.travelService = travelService;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(TravelServiceChannels.travelServiceChannel)
                .onMessage(RejectTripCommand.class, this::rejectTrip)
                .onMessage(ConfirmTripBooking.class, this::confirmBooking)
                .onMessage(RejectTripCancellationCommand.class, this::rejectCancellation)
                .onMessage(ConfirmTripCancellationCommand.class, this::confirmCancellation)
                .build();
    }

    private Message rejectTrip(CommandMessage<RejectTripCommand> command) {
        final long tripId = command.getCommand().getTripId();
        final RejectionReason rejectionReason = command.getCommand().getRejectionReason();
        logger.info("Received RejectTripCommand for tripId = " + tripId + ", rejection reason: " + rejectionReason);

        travelService.rejectTrip(tripId, rejectionReason);

        logger.info("Successfully rejected trip with tripId = " + tripId);
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmBooking(CommandMessage<ConfirmTripBooking> command) {
        final ConfirmTripBooking confirmTripBooking = command.getCommand();
        logger.info("Received ConfirmTripBooking for tripId = " + confirmTripBooking.getTripId());

        travelService.confirmTripBooking(confirmTripBooking.getTripId(), confirmTripBooking.getHotelId(),
                confirmTripBooking.getFlightId());

        logger.info("Successfully confirmed trip with tripId = " + confirmTripBooking.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message rejectCancellation(CommandMessage<RejectTripCancellationCommand> command) {
        final long tripId = command.getCommand().getTripId();
        final RejectionReason rejectionReason = command.getCommand().getRejectionReason();
        logger.info("Received RejectTripCancellation for tripId = " + tripId + ", rejection reason: " + rejectionReason);

        travelService.rejectTripCancellation(tripId, rejectionReason);

        logger.info("Successfully rejected trip cancellation for tripId = " + tripId);
        return CommandHandlerReplyBuilder.withSuccess();
    }

    private Message confirmCancellation(CommandMessage<ConfirmTripCancellationCommand> command) {
        final ConfirmTripCancellationCommand confirmTripCancellation = command.getCommand();
        logger.info("Received ConfirmTripCancellation for tripId = " + confirmTripCancellation.getTripId());

        travelService.confirmTripCancellation(confirmTripCancellation.getTripId());

        logger.info("Successfully confirmed trip cancellation for tripId = " + confirmTripCancellation.getTripId());
        return CommandHandlerReplyBuilder.withSuccess();
    }

}
