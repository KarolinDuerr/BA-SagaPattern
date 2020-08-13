package saga.eventuate.tram.travelservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;

@Component
public class TravelCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(TravelCommandHandler.class);

    @Autowired
    private ITravelService travelService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(TravelServiceChannels.travelServiceChannel)
                .onMessage(RejectTripCommand.class, this::rejectTrip)
                .build();
    }

    private Message rejectTrip(CommandMessage<RejectTripCommand> command) {
        return null;
    }

}
