package saga.eventuate.tram.travelservice.saga;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;

public class BookTripSaga implements SimpleSaga<BookTripSagaData> {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    private final SagaDefinition<BookTripSagaData> sagaDefinition;

    public BookTripSaga() {
        this.sagaDefinition =
                step()
                        .withCompensation(this::rejectBooking)
                        .step()
                        .invokeParticipant(this::bookHotel)
                        .build();
    }

    @Override
    public SagaDefinition<BookTripSagaData> getSagaDefinition() {
        return sagaDefinition;
    }

    private CommandWithDestination rejectBooking(BookTripSagaData bookTripSagaData) {
        logger.info("Rejecting the booking at the beginning of the saga."); // TODO

        return CommandWithDestinationBuilder.send(new RejectTripCommand(bookTripSagaData.getTripId()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }

    private CommandWithDestination bookHotel(BookTripSagaData bookTripSagaData) {

    }
}
