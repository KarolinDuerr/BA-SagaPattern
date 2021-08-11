package saga.eventuate.tram.travelservice.unittests.sagadefinition;

import io.eventuate.tram.sagas.testing.SagaUnitTestSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.CancelFlightBooking;
import saga.eventuate.tram.flightservice.api.dto.NoFlightAvailable;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.*;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.Location;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripDuration;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;
import saga.eventuate.tram.travelservice.saga.BookTripSagaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

@SpringBootTest
public class BookTripSagaUnitTest {

    private BookTripSagaData bookTripSagaData;

    @Before
    public void setUp() throws ParseException, TravelException {
        TripDuration tripDuration =
                new TripDuration(Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2021").toInstant()),
                        Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("12-12-2021").toInstant()));
        Location start = new Location("Germany", "Munich");
        Location destination = new Location("USA", "Tampa");
        TripInformation tripInformation = new TripInformation(tripDuration, start, destination, "Max Mustermann",
                "breakfast", 1);
        this.bookTripSagaData = new BookTripSagaData(1L, tripInformation);
    }

    //region Validate Saga sequence

    @Test
    public void bookTripSagaShouldBeSuccessful() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();

        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(bookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(bookFlightCommand)
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(confirmHotelBooking)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(confirmTripBooking)
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .successReply()
                .expectCompletedSuccessfully();
    }

    @Test
    public void bookTripSagaHotelFailureShouldCompensate() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(bookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .failureReply(new NoHotelAvailable(bookTripSagaData.getTripId()))
                .expect()
                    .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .successReply()
                .expectRolledBack();
    }

    @Test
    public void bookTripSagaFlightFailureShouldCompensate() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(bookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(bookFlightCommand)
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .failureReply(new NoFlightAvailable(bookTripSagaData.getTripId()))
                .expect()
                    .command(new CancelHotelBooking(bookTripSagaData.getHotelId(), bookTripSagaData.getTripId()))
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                .to(TravelServiceChannels.travelServiceChannel)
                    .andGiven()
                    .successReply()
                .expectRolledBack();
    }

    //endregion

    //region Check BookTripSagaData content

    @Test
    public void successfulBookTripSagaShouldIncludeHotelAndFlightId() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();

        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());

        Consumer<BookTripSagaData> bookTripSagaDataConsumer = bookTripSagaData -> {
            Assert.assertNull(bookTripSagaData.getRejectionReason());
//            Assert.assertNotEquals(-1, bookTripSagaData.getHotelId()); // TODO
//            Assert.assertNotEquals(-1, bookTripSagaData.getFlightId());
        };

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                .command(bookHotelRequest)
                .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                .successReply()
                .expect()
                .command(bookFlightCommand)
                .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                .successReply()
                .expect()
                .command(confirmHotelBooking)
                .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                .successReply()
                .expect()
                .command(confirmTripBooking)
                .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                .successReply()
                .expectCompletedSuccessfully()
                .assertSagaData(bookTripSagaDataConsumer);
    }

    @Test
    public void bookTripSagaHotelFailureShouldSetNoHotelAvailableRejectionReason() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();

        Consumer<BookTripSagaData> bookTripSagaDataConsumer = bookTripSagaData -> {
            Assert.assertEquals(RejectionReason.NO_HOTEL_AVAILABLE, bookTripSagaData.getRejectionReason());
            Assert.assertEquals(-1, bookTripSagaData.getHotelId());
            Assert.assertEquals(-1, bookTripSagaData.getFlightId());
        };

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                .command(bookHotelRequest)
                .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                .failureReply(new NoHotelAvailable(bookTripSagaData.getTripId()))
                .expect()
                .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                .successReply()
                .expectRolledBack()
                .assertSagaData(bookTripSagaDataConsumer);
    }

    @Test
    public void bookTripSagaFlightFailureShouldSetNoFlightAvailableRejectionReason() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();


        Consumer<BookTripSagaData> bookTripSagaDataConsumer = bookTripSagaData -> {
            Assert.assertEquals(RejectionReason.NO_FLIGHT_AVAILABLE, bookTripSagaData.getRejectionReason());
            Assert.assertEquals(-1, bookTripSagaData.getHotelId());
            Assert.assertEquals(-1, bookTripSagaData.getFlightId());
        };

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                .command(bookHotelRequest)
                .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                .successReply()
                .expect()
                .command(bookFlightCommand)
                .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                .failureReply(new NoFlightAvailable(bookTripSagaData.getTripId()))
                .expect()
                .command(new CancelHotelBooking(bookTripSagaData.getHotelId(), bookTripSagaData.getTripId()))
                .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                .successReply()
                .expect()
                .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                .successReply()
                .expectRolledBack()
                .assertSagaData(bookTripSagaDataConsumer);
    }

    //endregion


    //region Testing scenarios that should not happen, however to make sure the Saga definition corresponds to expectations

    @Test
    public void bookTripSagaConfirmHotelFailureShouldCompensate() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();

        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(bookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(bookFlightCommand)
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(confirmHotelBooking)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .failureReply()
                .expect()
                    .command(new CancelFlightBooking(bookTripSagaData.getFlightId(), bookTripSagaData.getTripId()))
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(new CancelHotelBooking(bookTripSagaData.getHotelId(), bookTripSagaData.getTripId()))
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .successReply()
                .expectRolledBack();
    }

    @Test
    public void bookTripSagaConfirmTripFailureShouldCompensate() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightCommand bookFlightCommand = bookTripSagaData.makeBookFlightCommand();

        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());

        // execute and verify
        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(bookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(bookFlightCommand)
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(confirmHotelBooking)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(confirmTripBooking)
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .failureReply()
                .expect()
                    .command(new CancelFlightBooking(bookTripSagaData.getFlightId(), bookTripSagaData.getTripId()))
                    .to(FlightServiceChannels.flightServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(new CancelHotelBooking(bookTripSagaData.getHotelId(), bookTripSagaData.getTripId()))
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .successReply()
                .expectRolledBack();
    }

    //endregion

    private BookTripSaga makeBookTripSaga() {
        return new BookTripSaga();
    }

}
