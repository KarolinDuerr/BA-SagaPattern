package saga.eventuate.tram.travelservice.saga;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saga.eventuate.tram.flightservice.api.dto.BookFlightResponse;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelResponse;
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
                            .onReply(BookHotelResponse.class, this::handleBookHotelReply)
                            .withCompensation(this::cancelHotel)
                        .step()
                            .invokeParticipant(this::bookFlight)
                            .onReply(BookFlightResponse.class, this::handleBookFlightReply)
                        .step()
                            .invokeParticipant(this::confirmHotel)
                        .step()
                            .invokeParticipant(this::confirmTrip)
                        .build();
    }

    @Override
    public SagaDefinition<BookTripSagaData> getSagaDefinition() {
        return sagaDefinition;
    }

    private CommandWithDestination rejectBooking(BookTripSagaData bookTripSagaData) {
        logger.info("Rejecting the trip booking.");

        return CommandWithDestinationBuilder.send(new RejectTripCommand(bookTripSagaData.getTripId()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }

    private CommandWithDestination bookHotel(BookTripSagaData bookTripSagaData) {
        return null;
    }

    private void handleBookHotelReply(BookTripSagaData bookTripSagaData, BookHotelResponse bookHotelResponse) {

    }

    private CommandWithDestination cancelHotel(BookTripSagaData bookTripSagaData) {
        return null;
    }

    private CommandWithDestination bookFlight(BookTripSagaData bookTripSagaData) {
        return null;
    }

    private void handleBookFlightReply(BookTripSagaData bookTripSagaData, BookFlightResponse bookFlightResponse) {
    }

    private CommandWithDestination confirmHotel(BookTripSagaData bookTripSagaData) {
        return null;
    }

    private CommandWithDestination confirmTrip(BookTripSagaData bookTripSagaData) {
        return null;
    }

}
