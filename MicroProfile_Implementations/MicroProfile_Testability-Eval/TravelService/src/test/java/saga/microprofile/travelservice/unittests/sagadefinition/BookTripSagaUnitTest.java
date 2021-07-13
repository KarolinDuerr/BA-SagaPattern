package saga.microprofile.travelservice.unittests.sagadefinition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import saga.microprofile.flightservice.api.dto.BookFlightRequest;
import saga.microprofile.flightservice.api.dto.BookFlightResponse;
import saga.microprofile.hotelservice.api.dto.BookHotelRequest;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.ConfirmHotelBooking;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.error.TravelException;
import saga.microprofile.travelservice.model.Location;
import saga.microprofile.travelservice.model.TripDuration;
import saga.microprofile.travelservice.model.TripInformation;
import saga.microprofile.travelservice.saga.BookTripSaga;
import saga.microprofile.travelservice.saga.BookTripSagaData;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.ParseException;
import java.time.LocalDate;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

//@MicroShedTest
public class BookTripSagaUnitTest {

    private BookTripSaga bookTripSaga;

    private BookTripSagaData bookTripSagaData;

    private Client mockedServiceClient;

    private String flightServiceUri = "flight";

    private String hotelServiceUri = "hotel";

    private String travelServiceUri = "travel";

    private final URI lraId = URI.create("http://localhost:8090/test/lraId/12345");

    @Before
    public void setUp() throws TravelException {
        this.bookTripSagaData = makeBookTripSagaData();
        mockedServiceClient = Mockito.mock(Client.class);
        this.bookTripSaga = new BookTripSaga(bookTripSagaData, flightServiceUri, hotelServiceUri, travelServiceUri,
                String.valueOf(lraId), mockedServiceClient);
    }

    @Test
    public void bookTripSagaShouldCallHotelFlightTravelService() {
        // setup
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();
        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(1L, 1L);
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(1L, 1L, 1L);

        WebTarget mockedHotelServiceBookTarget = Mockito.mock(WebTarget.class, Answers.RETURNS_DEEP_STUBS);
        WebTarget mockedHotelServiceConfirmTarget = Mockito.mock(WebTarget.class, Answers.RETURNS_DEEP_STUBS);
        BookHotelResponse hotelResponse = new BookHotelResponse(1L, 1L, "Test Hotel", "PENDING");
        createStubsForHotelServiceCalls(mockedHotelServiceBookTarget, hotelResponse, mockedHotelServiceConfirmTarget);

        WebTarget mockedFlightServiceTarget = Mockito.mock(WebTarget.class, Answers.RETURNS_DEEP_STUBS);
        BookFlightResponse flightResponse = new BookFlightResponse(1L, 1L, "BOOKED");
        createStubsForFlightServiceCalls(mockedFlightServiceTarget, flightResponse);
        WebTarget mockedTravelServiceTarget = Mockito.mock(WebTarget.class, Answers.RETURNS_DEEP_STUBS);
        createStubsForTravelServiceCalls(mockedTravelServiceTarget);


        // execute
        bookTripSaga.run();

        // verify
        InOrder inOrder = Mockito.inOrder(mockedHotelServiceBookTarget, mockedFlightServiceTarget,
                mockedHotelServiceConfirmTarget, mockedTravelServiceTarget);
        // verify sequence of calls
        inOrder.verify(mockedHotelServiceBookTarget).request();
        inOrder.verify(mockedFlightServiceTarget).request();
        inOrder.verify(mockedHotelServiceConfirmTarget).request();
        inOrder.verify(mockedTravelServiceTarget).request();
        inOrder.verifyNoMoreInteractions();

        // TODO: test that correct header is used: .header(LRA_HTTP_CONTEXT_HEADER, lraId))
        // verify call requests
        Mockito.verify(mockedHotelServiceBookTarget.request().header(Mockito.anyString(), Mockito.anyString()),
                Mockito.times(1))
                .post(Mockito.eq(Entity.entity(bookHotelRequest, MediaType.APPLICATION_JSON)));

        Mockito.verify(mockedFlightServiceTarget.request().header(Mockito.anyString(), Mockito.anyString()),
                Mockito.times(1))
                .post(Mockito.eq(Entity.entity(bookFlightRequest, MediaType.APPLICATION_JSON)));

        Mockito.verify(mockedHotelServiceConfirmTarget.request().header(Mockito.anyString(), Mockito.anyString()),
                Mockito.times(1))
                .put(Mockito.eq(Entity.entity(confirmHotelBooking, MediaType.APPLICATION_JSON)));

        Mockito.verify(mockedTravelServiceTarget.request().header(Mockito.anyString(), Mockito.anyString()),
                Mockito.times(1))
                .put(Mockito.eq(Entity.entity(confirmTripBooking, MediaType.APPLICATION_JSON)));

        // verify data
        Assert.assertEquals("Hotel ID has different value than expected. ", hotelResponse.getBookingId(),
                bookTripSagaData.getHotelId());
        Assert.assertEquals("Flight ID has different value than expected. ", flightResponse.getFlightBookingId(),
                bookTripSagaData.getFlightId());
        Assert.assertNull("RejectionReason within BookTripSagaData object should be null.",
                bookTripSagaData.getRejectionReason());
    }

    private BookTripSagaData makeBookTripSagaData() throws TravelException {
        TripDuration tripDuration = new TripDuration(LocalDate.parse("2021-12-01"), LocalDate.parse("2021-12-12"));
        Location start = new Location("Germany", "Munich");
        Location destination = new Location("USA", "Tampa");
        TripInformation tripInformation = new TripInformation(lraId, tripDuration, start, destination, "Max Mustermann",
                "breakfast", 1);
        return new BookTripSagaData(1L, tripInformation);
    }

    private void createStubsForHotelServiceCalls(final WebTarget mockedHotelServiceBookTarget,
                                                 final BookHotelResponse bookHotelResponse,
                                                 final WebTarget mockedHotelServiceConfirmTarget) {
        // Book Hotel
        Mockito.when(mockedServiceClient.target(Mockito.eq(hotelServiceUri))).thenReturn(mockedHotelServiceBookTarget);
        Response mockedHotelResponse = Mockito.mock(Response.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedHotelServiceBookTarget.request().header(Mockito.anyString(), Mockito.anyString())
                .post(Mockito.any(Entity.class))).thenReturn(mockedHotelResponse);
        Mockito.when(mockedHotelResponse.getStatusInfo().getStatusCode()).thenReturn(Response.Status.OK.getStatusCode());
        Mockito.when(mockedHotelResponse.readEntity(Mockito.eq(BookHotelResponse.class))).thenReturn(bookHotelResponse);

        // Confirm Hotel
        String confirmUri = String.format("%s/%s/confirm", hotelServiceUri, bookTripSagaData.getTripId());
        Mockito.when(mockedServiceClient.target(Mockito.eq(confirmUri))).thenReturn(mockedHotelServiceConfirmTarget);
        Response mockedConfirmHotelResponse = Mockito.mock(Response.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedHotelServiceConfirmTarget.request().header(Mockito.anyString(), Mockito.anyString())
                .put(Mockito.any(Entity.class))).thenReturn(mockedConfirmHotelResponse);
        Mockito.when(mockedConfirmHotelResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
    }

    private void createStubsForFlightServiceCalls(final WebTarget mockedFlightServiceTarget,
                                                  final BookFlightResponse bookFlightResponse) {
        Mockito.when(mockedServiceClient.target(Mockito.eq(flightServiceUri))).thenReturn(mockedFlightServiceTarget);
        Response mockedFlightResponse = Mockito.mock(Response.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedFlightServiceTarget.request().header(Mockito.anyString(), Mockito.anyString())
                .post(Mockito.any(Entity.class))).thenReturn(mockedFlightResponse);
        Mockito.when(mockedFlightResponse.getStatusInfo().getStatusCode()).thenReturn(Response.Status.OK.getStatusCode());
        Mockito.when(mockedFlightResponse.readEntity(Mockito.eq(BookFlightResponse.class))).thenReturn(bookFlightResponse);
    }

    private void createStubsForTravelServiceCalls(final WebTarget mockedTravelServiceTarget) {
        // Confirm Trip
        String confirmUri = String.format("%s/trips/%s/confirm", travelServiceUri, bookTripSagaData.getTripId());
        Mockito.when(mockedServiceClient.target(Mockito.eq(confirmUri))).thenReturn(mockedTravelServiceTarget);
        Response mockedConfirmTripResponse = Mockito.mock(Response.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedTravelServiceTarget.request().header(Mockito.anyString(), Mockito.anyString())
                .put(Mockito.any(Entity.class)))
                .thenReturn(mockedConfirmTripResponse);
        Mockito.when(mockedConfirmTripResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
    }

}
