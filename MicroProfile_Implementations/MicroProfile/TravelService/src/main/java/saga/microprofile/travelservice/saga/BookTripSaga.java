package saga.microprofile.travelservice.saga;

import saga.microprofile.hotelservice.api.dto.BookHotelRequest;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.NoHotelAvailable;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.error.ErrorMessage;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class BookTripSaga implements Runnable {

    private static final Logger logger = Logger.getLogger(BookTripSaga.class.toString());

    private final BookTripSagaData bookTripSagaData;

    private final Client hotelServiceClient;

    private final Client flightServiceClient;

    private final Client travelServiceClient;

    private final String flightServiceBaseUri;

    private final String hotelServiceBaseUri;

    private final String travelServiceBaseUri;

    public BookTripSaga(final BookTripSagaData bookTripSagaData, final String flightServiceUri,
                        final String hotelServiceUri, final String travelServiceUri) {
        this.bookTripSagaData = bookTripSagaData;
        this.hotelServiceClient = ClientBuilder.newClient();
        this.flightServiceClient = ClientBuilder.newClient();
        this.travelServiceClient = ClientBuilder.newClient();

        this.flightServiceBaseUri = flightServiceUri;
        this.hotelServiceBaseUri = hotelServiceUri;
        this.travelServiceBaseUri = travelServiceUri;
    }

    @Override
    public void run() {
        logger.info("Started BookTripSaga Thread.");

        bookHotel();
        confirmTripBooking();
    }

    private void bookHotel() {
        logger.info("Trying to book hotel.");
        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelServiceBaseUri);;
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        Response hotelResponse = hotelServiceTarget.request().post(Entity.entity(bookHotelRequest,
                MediaType.APPLICATION_JSON_TYPE));
        handleHotelBookingResponse(hotelResponse, bookTripSagaData);
    }

    private void confirmTripBooking() {
        logger.info("Trying to confirm the trip.");
        String travelConfirmUri = String.format("%s/trips/%s/confirm", travelServiceBaseUri, bookTripSagaData.getTripId());
        WebTarget travelServiceTarget = travelServiceClient.target(travelConfirmUri);
        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());
        Response confirmTripResponse = travelServiceTarget.request().put(Entity.entity(confirmTripBooking,
                MediaType.APPLICATION_JSON_TYPE));
        logger.info("Received from TravelService: " + confirmTripResponse.getStatus());
    }

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
                logger.info(hotelResponseFailure.toString());
                NoHotelAvailable noHotelAvailable = jsonb.fromJson(hotelResponseFailure.toString(), NoHotelAvailable.class);
                logger.info("Received response NoHotelAvailable: " + noHotelAvailable);
            } catch (JsonbException ignore) {
                try {
                    ErrorMessage errorMessage = jsonb.fromJson(hotelResponseFailure.toString(), ErrorMessage.class);
                    logger.info("Received errorMessage: " + errorMessage);
                } catch (JsonbException jsonbException) {
                    logger.warning("Hotel answer could not be processed: " + jsonbException.toString());
                }
            }
//            try {
//                NoHotelAvailable noHotelAvailableResponse = hotelResponse.readEntity(NoHotelAvailable.class);
//                logger.info("No Hotel available: " + noHotelAvailableResponse.toString());
//                bookTripSagaData.setRejectionReason(RejectionReason.NO_HOTEL_AVAILABLE);
//            } catch (ProcessingException processingException) {
//                ErrorMessage errorMessage = hotelResponse.readEntity(ErrorMessage.class);
//                logger.warning("Error message received from HotelService: " + errorMessage.toString());
//            }
//            Object test = hotelResponse.readEntity(Object.class);
//            logger.info("Test: " + test.toString());
//            logger.info("Test class instance: " + (test instanceof NoHotelAvailable));
        } else {
            logger.warning("Something went wrong when receiving hotel answer: " + hotelResponse.getStatusInfo().getStatusCode());
        }
    }
}
