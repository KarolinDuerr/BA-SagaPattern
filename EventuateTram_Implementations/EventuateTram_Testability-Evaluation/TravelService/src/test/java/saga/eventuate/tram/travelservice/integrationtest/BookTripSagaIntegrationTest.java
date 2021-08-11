package saga.eventuate.tram.travelservice.integrationtest;

import org.junit.Before;
import org.junit.Test;
import saga.eventuate.tram.flightservice.api.dto.LocationDTO;
import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.Location;
import saga.eventuate.tram.travelservice.model.TripDuration;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;
import saga.eventuate.tram.travelservice.saga.BookTripSagaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO
public class BookTripSagaIntegrationTest {

    private BookTripSagaData bookTripSagaData;
    private DestinationDTO destinationDTO;
    private StayDurationDTO stayDurationDTO;
    private LocationDTO homeLocationDTO;
    private LocationDTO destinationLocationDTO;

    @Before
    public void setUp() throws TravelException, ParseException {
        TripDuration tripDuration =
                new TripDuration(Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2021").toInstant()),
                        Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("12-12-2021").toInstant()));
        Location start = new Location("Germany", "Munich");
        Location destination = new Location("USA", "Tampa");
        TripInformation tripInformation = new TripInformation(tripDuration, start, destination, "Max Mustermann",
                "breakfast", 1);
        this.bookTripSagaData = new BookTripSagaData(1L, tripInformation);

        destinationDTO = new DestinationDTO(destination.getCountry(), destination.getCity());
        stayDurationDTO = new StayDurationDTO(tripDuration.getStart(), tripDuration.getEnd());
        homeLocationDTO = new LocationDTO(start.getCountry(), start.getCity());
        destinationLocationDTO = new LocationDTO(destination.getCountry(), destination.getCity());
    }

    @Test
    public void bookTripSuccessful() {
        // setup

        // execute

        // verify
    }

    private BookTripSaga makeBookTripSaga() {
        return new BookTripSaga();
    }

}
