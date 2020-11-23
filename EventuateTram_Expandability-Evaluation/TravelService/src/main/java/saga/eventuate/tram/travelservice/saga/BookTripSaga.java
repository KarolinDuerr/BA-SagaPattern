package saga.eventuate.tram.travelservice.saga;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saga.eventuate.tram.customerservice.api.CustomerServiceChannels;
import saga.eventuate.tram.customerservice.api.dto.CustomerNotFound;
import saga.eventuate.tram.customerservice.api.dto.ValidateCustomerCommand;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightResponse;
import saga.eventuate.tram.flightservice.api.dto.CancelFlightBooking;
import saga.eventuate.tram.flightservice.api.dto.NoFlightAvailable;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelResponse;
import saga.eventuate.tram.hotelservice.api.dto.CancelHotelBooking;
import saga.eventuate.tram.hotelservice.api.dto.ConfirmHotelBooking;
import saga.eventuate.tram.hotelservice.api.dto.NoHotelAvailable;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;
import saga.eventuate.tram.travelservice.model.RejectionReason;

/**
 * The defined Saga Orchestrator for booking a trip.
 */
public class BookTripSaga implements SimpleSaga<BookTripSagaData> {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    private final SagaDefinition<BookTripSagaData> sagaDefinition;

    public BookTripSaga() {
        this.sagaDefinition =
                step()
                        .withCompensation(this::rejectBooking)
                        .step()
                        .invokeParticipant(this::validateCustomer)
                        .onReply(CustomerNotFound.class, this::handleCustomerNotFoundReply)
                        // no compensation necessary as a validation does not change anything 
                        .step()
                        .invokeParticipant(this::bookHotel)
                        .onReply(BookHotelResponse.class, this::handleBookHotelReply)
                        .onReply(NoHotelAvailable.class, this::handleNoHotelAvailableReply)
                        .withCompensation(this::cancelHotel)
                        .step()
                        .invokeParticipant(this::bookFlight)
                        .onReply(BookFlightResponse.class, this::handleBookFlightReply)
                        .onReply(NoFlightAvailable.class, this::handleNoFlightAvailableReply)
                        .withCompensation(this::cancelFlight)
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

        return CommandWithDestinationBuilder.send(new RejectTripCommand(bookTripSagaData.getTripId(),
                bookTripSagaData.getRejectionReason()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }

    private CommandWithDestination validateCustomer(BookTripSagaData bookTripSagaData) {
        logger.info("Validating the customer.");

        return CommandWithDestinationBuilder.send(new ValidateCustomerCommand(bookTripSagaData.getTripInformation().getCustomerId()))
                .to(CustomerServiceChannels.customerServiceChannel)
                .build();
    }

    private void handleCustomerNotFoundReply(BookTripSagaData bookTripSagaData, CustomerNotFound customerNotFound) {
        logger.debug("Received provoked customer validation failed response for trip " + bookTripSagaData.getTripId());
        bookTripSagaData.setRejectionReason(RejectionReason.CUSTOMER_VALIDATION_FAILED);
    }

    private CommandWithDestination bookHotel(BookTripSagaData bookTripSagaData) {
        logger.info("Trying to book a hotel.");

        return CommandWithDestinationBuilder.send(bookTripSagaData.makeBookHotelRequest())
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private void handleBookHotelReply(BookTripSagaData bookTripSagaData, BookHotelResponse bookHotelResponse) {
        logger.info("Hotel has been successfully booked: " + bookHotelResponse.getBookingId());

        bookTripSagaData.setHotelId(bookHotelResponse.getBookingId());
    }

    private void handleNoHotelAvailableReply(BookTripSagaData bookTripSagaData, NoHotelAvailable noHotelAvailable) {
        logger.debug("Received provoked NoHotelAvailable response for trip " + noHotelAvailable.getTripId());
        bookTripSagaData.setRejectionReason(RejectionReason.NO_HOTEL_AVAILABLE);
    }

    private CommandWithDestination cancelHotel(BookTripSagaData bookTripSagaData) {
        logger.info("Compensating hotel booking --> cancel");

        return CommandWithDestinationBuilder.send(new CancelHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private CommandWithDestination bookFlight(BookTripSagaData bookTripSagaData) {
        logger.info("Trying to book a flight.");

        return CommandWithDestinationBuilder.send(bookTripSagaData.makeBookFlightCommand())
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

    private void handleBookFlightReply(BookTripSagaData bookTripSagaData, BookFlightResponse bookFlightResponse) {
        logger.info("Flight has been successfully booked: " + bookFlightResponse.getFlightBookingId());

        bookTripSagaData.setFlightId(bookFlightResponse.getFlightBookingId());
    }

    private void handleNoFlightAvailableReply(BookTripSagaData bookTripSagaData, NoFlightAvailable noFlightAvailable) {
        logger.debug("Received provoked NoFlightAvailable response for trip " + noFlightAvailable.getTripId());
        bookTripSagaData.setRejectionReason(RejectionReason.NO_FLIGHT_AVAILABLE);
    }

    private CommandWithDestination cancelFlight(BookTripSagaData bookTripSagaData) {
        logger.info("Compensating flight booking --> cancel");

        return CommandWithDestinationBuilder.send(new CancelFlightBooking(bookTripSagaData.getFlightId(),
                bookTripSagaData.getTripId()))
                .to(FlightServiceChannels.flightServiceChannel)
                .build();
    }

    private CommandWithDestination confirmHotel(BookTripSagaData bookTripSagaData) {
        logger.info("Confirming the hotel booking: " + bookTripSagaData.getHotelId());

        return CommandWithDestinationBuilder.send(new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .build();
    }

    private CommandWithDestination confirmTrip(BookTripSagaData bookTripSagaData) {
        logger.info("Confirming the trip booking: " + bookTripSagaData.getTripId());

        return CommandWithDestinationBuilder.send(new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId()))
                .to(TravelServiceChannels.travelServiceChannel)
                .build();
    }
}
