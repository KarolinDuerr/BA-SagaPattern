package saga.eventuate.tram.travelservice.unittests;

import io.eventuate.tram.sagas.testing.SagaUnitTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.LocationDTO;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.*;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
import saga.eventuate.tram.travelservice.command.RejectTripCommand;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.Location;
import saga.eventuate.tram.travelservice.model.TripDuration;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;
import saga.eventuate.tram.travelservice.saga.BookTripSagaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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
                    .failureReply(new NoHotelAvailable(bookTripSagaData.getTripId()))
                .expect()
                    .command(new RejectTripCommand(bookTripSagaData.getTripId(), bookTripSagaData.getRejectionReason()))
                    .to(TravelServiceChannels.travelServiceChannel)
                .andGiven()
                    .successReply()
                .expectRolledBack();
    }

    private BookTripSaga makeBookTripSaga() {
        return new BookTripSaga();
    }

}
