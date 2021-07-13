package saga.microprofile.travelservice.saga;

import saga.microprofile.flightservice.api.dto.BookFlightRequest;
import saga.microprofile.flightservice.api.dto.BookFlightResponse;
import saga.microprofile.flightservice.api.dto.NoFlightAvailable;
import saga.microprofile.hotelservice.api.dto.BookHotelRequest;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.ConfirmHotelBooking;
import saga.microprofile.hotelservice.api.dto.NoHotelAvailable;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.api.dto.RejectionReason;
import saga.microprofile.travelservice.error.ErrorMessage;

import javax.annotation.PreDestroy;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

public class BookTripSaga implements Runnable {

    private static final Logger logger = Logger.getLogger(BookTripSaga.class.toString());

    private final BookTripSagaData bookTripSagaData;

    private final Client hotelServiceClient;

    private final Client flightServiceClient;

    private final Client travelServiceClient;

    private final String flightServiceBaseUri;

    private final String hotelServiceBaseUri;

    private final String travelServiceBaseUri;

    private final String lraId;

    public BookTripSaga(final BookTripSagaData bookTripSagaData, final String flightServiceUri,
                        final String hotelServiceUri, final String travelServiceUri, final String lraId) {
        this.bookTripSagaData = bookTripSagaData;
        this.lraId = lraId;

        this.hotelServiceClient = ClientBuilder.newClient();
        this.flightServiceClient = ClientBuilder.newClient();
        this.travelServiceClient = ClientBuilder.newClient();

        this.flightServiceBaseUri = flightServiceUri;
        this.hotelServiceBaseUri = hotelServiceUri;
        this.travelServiceBaseUri = travelServiceUri;
    }

    /**
     * Needed for testing purposes in order to be able to mock the {@link Client}s.
     *
     * @param testClient Used as client for all three service calls (HotelService, FlightService, TravelService)s
     */
    public BookTripSaga(final BookTripSagaData bookTripSagaData, final String flightServiceUri,
                        final String hotelServiceUri, final String travelServiceUri, final String lraId,
                        final Client testClient) {
        this.bookTripSagaData = bookTripSagaData;
        this.lraId = lraId;

        this.hotelServiceClient = testClient;
        this.flightServiceClient = testClient;
        this.travelServiceClient = testClient;

        this.flightServiceBaseUri = flightServiceUri;
        this.hotelServiceBaseUri = hotelServiceUri;
        this.travelServiceBaseUri = travelServiceUri;
    }

    @Override
    public void run() {
        logger.info("Started BookTripSaga Thread.");

        bookHotel();
        bookFlight();

        if (checkForFailures()) {
            logger.info("Aborting Saga due to a failure. Rejection reason of trip: " + bookTripSagaData.getRejectionReason());
            return;
        }

        confirmHotelBooking();
        confirmTripBooking();
    }

    private void bookHotel() {
        logger.info("Trying to book a hotel.");
        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelServiceBaseUri);
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        // setting context header since new thread does not know about the current LRA context
        Response hotelResponse =
                hotelServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).post(Entity.entity(bookHotelRequest, MediaType.APPLICATION_JSON));
        handleHotelBookingResponse(hotelResponse, bookTripSagaData);
//        test(hotelResponse, NoHotelAvailable.class); // TODO
    }

    private void bookFlight() {
        if (checkForFailures()) {
            return;
        }

        logger.info("Trying to book a flight.");
        WebTarget flightServiceTarget = flightServiceClient.target(flightServiceBaseUri);
        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();
        // setting context header since new thread does not know about the current LRA context
        Response flightResponse =
                flightServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).post(Entity.entity(bookFlightRequest, MediaType.APPLICATION_JSON));
        handleFlightBookingResponse(flightResponse, bookTripSagaData);
//        test(flightResponse, NoFlightAvailable.class); // TODO
    }

    private void confirmHotelBooking() {
        logger.info("Trying to confirm the hotel booking.");
        String hotelConfirmUri = String.format("%s/%s/confirm", hotelServiceBaseUri,
                bookTripSagaData.getTripId()); // TODO prüfen --> nicht hotelBookingId?
        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelConfirmUri);
        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        // setting context header since new thread does not know about the current LRA context
        Response confirmTripResponse =
                hotelServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).put(Entity.entity(confirmHotelBooking, MediaType.APPLICATION_JSON_TYPE));
        logger.info("Received from HotelService: " + confirmTripResponse.getStatus());
    }

    private void confirmTripBooking() {
        logger.info("Trying to confirm the trip.");
        String travelConfirmUri = String.format("%s/trips/%s/confirm", travelServiceBaseUri,
                bookTripSagaData.getTripId());
        WebTarget travelServiceTarget = travelServiceClient.target(travelConfirmUri);
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());
        // setting context header since new thread does not know about the current LRA context
        Response confirmTripResponse =
                travelServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).put(Entity.entity(confirmTripBooking, MediaType.APPLICATION_JSON));
        logger.info("Received from TravelService: " + confirmTripResponse.getStatus());
    }

    // TODO refactor handle methods
    private void handleHotelBookingResponse(final Response hotelResponse, final BookTripSagaData bookTripSagaData) {
        if (hotelResponse.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()) {
            BookHotelResponse bookHotelResponse = hotelResponse.readEntity(BookHotelResponse.class);
            logger.info("Received from HotelService: " + bookHotelResponse);
            long hotelId = bookHotelResponse == null ? -1 : bookHotelResponse.getBookingId();
            bookTripSagaData.setHotelId(hotelId);
        } else if (hotelResponse.getStatusInfo().getStatusCode() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            JsonObject hotelResponseFailure = hotelResponse.readEntity(JsonObject.class);
            Jsonb jsonb = JsonbBuilder.create();
            try {
                NoHotelAvailable noHotelAvailable = jsonb.fromJson(hotelResponseFailure.toString(),
                        NoHotelAvailable.class);
                selectRejectionReason(noHotelAvailable);
            } catch (JsonbException ignore) {
                handleJsonbException(jsonb, hotelResponseFailure);
            }
        } else {
            logger.warning("Something went wrong when receiving hotel answer: " + hotelResponse.getStatusInfo().getStatusCode());
        }
    }

    // TODO refactor handle methods
    private void handleFlightBookingResponse(final Response flightResponse, final BookTripSagaData bookTripSagaData) {
        if (flightResponse.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()) {
            BookFlightResponse bookFlightResponse = flightResponse.readEntity(BookFlightResponse.class);
            logger.info("Received from FlightService: " + bookFlightResponse);
            long flightId = bookFlightResponse == null ? -1 : bookFlightResponse.getFlightBookingId();
            bookTripSagaData.setFlightId(flightId);
        } else if (flightResponse.getStatusInfo().getStatusCode() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            JsonObject hotelResponseFailure = flightResponse.readEntity(JsonObject.class);
            Jsonb jsonb = JsonbBuilder.create();
            try {
                NoFlightAvailable noFlightAvailable = jsonb.fromJson(hotelResponseFailure.toString(),
                        NoFlightAvailable.class);
                selectRejectionReason(noFlightAvailable);
            } catch (JsonbException ignore) {
                handleJsonbException(jsonb, hotelResponseFailure);
            }
        } else {
            logger.warning("Something went wrong when receiving flight answer: " + flightResponse.getStatusInfo().getStatusCode());
        }
    }

    private boolean checkForFailures() {
        return bookTripSagaData.getRejectionReason() != null;
    }

    // TODO
    private <T> void test(final Response response, final Class<T> type) {
        JsonObject responseFailure = response.readEntity(JsonObject.class);
        Jsonb jsonb = JsonbBuilder.create();
        try {
            T failureResponseObject = jsonb.fromJson(response.toString(), type);
            selectRejectionReason(failureResponseObject);
        } catch (JsonbException ignore) {
            handleJsonbException(jsonb, responseFailure);
        }
    }

    private void handleJsonbException(final Jsonb jsonb, final JsonObject failureResponse) {
        try {
            ErrorMessage errorMessage = jsonb.fromJson(failureResponse.toString(), ErrorMessage.class);
            logger.info("Received errorMessage: " + errorMessage);
        } catch (JsonbException jsonbException) {
            logger.warning("Hotel answer could not be processed: " + jsonbException.getMessage());
        }
    }

    private void selectRejectionReason(final Object failureResponse) {
        if (failureResponse instanceof NoHotelAvailable) {
            bookTripSagaData.setRejectionReason(RejectionReason.NO_HOTEL_AVAILABLE);
            logger.info("Received response NoHotelAvailable: " + failureResponse);
            return;
        } else if (failureResponse instanceof NoFlightAvailable) {
            bookTripSagaData.setRejectionReason(RejectionReason.NO_FLIGHT_AVAILABLE);
            logger.info("Received response NoFlightAvailable: " + failureResponse);
            return;
        }

        bookTripSagaData.setRejectionReason(RejectionReason.REASON_UNKNOWN);
        logger.info("Received response could not be identified: " + failureResponse);
    }

    @PreDestroy
    public void cleanUp() {
        logger.info("Clean-up: Closing the clients.");
        hotelServiceClient.close();
        flightServiceClient.close();
        travelServiceClient.close();
    }
}
