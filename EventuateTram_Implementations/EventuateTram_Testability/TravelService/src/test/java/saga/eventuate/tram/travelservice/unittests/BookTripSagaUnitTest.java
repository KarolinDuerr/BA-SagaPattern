package saga.eventuate.tram.travelservice.unittests;

import io.eventuate.tram.sagas.testing.SagaUnitTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import saga.eventuate.tram.flightservice.api.FlightServiceChannels;
import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.LocationDTO;
import saga.eventuate.tram.hotelservice.api.HotelServiceChannels;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.ConfirmHotelBooking;
import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.travelservice.api.TravelServiceChannels;
import saga.eventuate.tram.travelservice.command.ConfirmTripBooking;
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
    private BookHotelRequest mockedBookHotelRequest;
    private BookFlightCommand mockedBookFlightCommand;
    private ConfirmHotelBooking mockedConfirmHotelBooking;
    private ConfirmTripBooking mockedConfirmTripBooking;

    @Before
    public void setUp() throws TravelException, ParseException {
        TripDuration mockedTripDuration = Mockito.mock(TripDuration.class);
        Location mockedStartLocation = Mockito.mock(Location.class);
        Location mockedDestinationLocation = Mockito.mock(Location.class);
        TripInformation mockedTripInformation = Mockito.mock(TripInformation.class);
        this.bookTripSagaData = new BookTripSagaData(1L, mockedTripInformation);
//        this.bookTripSagaData = Mockito.mock(BookTripSagaData.class);

        Mockito.doReturn(mockedBookHotelRequest).when(bookTripSagaData).makeBookHotelRequest();
        Mockito.doReturn(mockedBookFlightCommand).when(bookTripSagaData).makeBookFlightCommand();


//        destinationDTO = Mockito.mock(DestinationDTO.class);
//        stayDurationDTO = Mockito.mock(StayDurationDTO.class);
//        homeLocationDTO = Mockito.mock(LocationDTO.class);
//        destinationLocationDTO = Mockito.mock(LocationDTO.class);
    }

    @Test
    public void bookTripSagaShouldBeSuccessful() {
        // setup
        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());

        SagaUnitTestSupport.given().saga(makeBookTripSaga(), bookTripSagaData)
                .expect()
                    .command(mockedBookHotelRequest)
                    .to(HotelServiceChannels.hotelServiceChannel)
                .andGiven()
                    .successReply()
                .expect()
                    .command(mockedBookFlightCommand)
                    .to(FlightServiceChannels.flightServiceChannel);
//                .andGiven()
//                    .successReply()
//                .expect()
//                    .command(confirmHotelBooking)
//                    .to(HotelServiceChannels.hotelServiceChannel)
//                .andGiven()
//                    .successReply()
//                .expect()
//                    .command(confirmTripBooking)
//                    .to(TravelServiceChannels.travelServiceChannel)
//                .andGiven()
//                    .successReply()
//                .expectCompletedSuccessfully();
    }

    private BookTripSaga makeBookTripSaga() {
        return new BookTripSaga();
    }

}
