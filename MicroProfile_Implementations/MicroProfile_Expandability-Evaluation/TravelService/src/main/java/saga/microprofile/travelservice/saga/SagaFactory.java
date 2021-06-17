package saga.microprofile.travelservice.saga;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@ApplicationScoped
public class SagaFactory {

    private static final Logger logger = Logger.getLogger(SagaFactory.class.toString());

    @Resource(lookup = "concurrent/threadFactory2")
    private ThreadFactory threadFactory; // TODO fix Thread for Docker?

    @Inject
    @ConfigProperty(name = "hotel.service.uri", defaultValue = "http://localhost:8081/api/hotels/bookings")
    private String hotelServiceBaseUri;

    @Inject
    @ConfigProperty(name = "flight.service.uri", defaultValue = "http://localhost:8082/api/flights/bookings")
    private String flightServiceBaseUri;

    @Inject
    @ConfigProperty(name = "customer.service.uri", defaultValue = "http://localhost:8084/api/customers")
    private String customerServiceBaseUri;

    @Inject
    @ConfigProperty(name = "travel.service.uri", defaultValue = "http://localhost:8090/api/travel")
    private String travelServiceBaseUri;

    public void startBookTripSaga(final BookTripSagaData bookTripSagaData, final String lraId) {
        logger.info("Starting BookTripSaga");
        threadFactory.newThread(new BookTripSaga(bookTripSagaData, flightServiceBaseUri, hotelServiceBaseUri,
                travelServiceBaseUri, customerServiceBaseUri, lraId)).start();
    }

//    // TODO remove when Docker is working
//
//    private BookTripSagaData bookTripSagaData;
//
//    private Client hotelServiceClient;
//
//    private Client flightServiceClient;
//
//    private Client travelServiceClient;
//
//    private String lraId;
//
//    public void startBookTripSaga(final BookTripSagaData bookTripSagaData, final String lraId) {
//        logger.info("Starting BookTripSaga");
//        this.bookTripSagaData = bookTripSagaData;
//        this.lraId = lraId;
//
//        this.hotelServiceClient = ClientBuilder.newClient();
//        this.flightServiceClient = ClientBuilder.newClient();
//        this.travelServiceClient = ClientBuilder.newClient();
//
//        run();
//    }
//
//    private void run() {
//        bookHotel();
//        bookFlight();
//        confirmHotelBooking();
//        confirmTripBooking();
//    }
//
//    private void bookHotel() {
//        logger.info("Trying to book a hotel.");
//        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelServiceBaseUri);
//        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
//        // setting context header since new thread does not know about the current LRA context
//        Response hotelResponse =
//                hotelServiceTarget.request().post(Entity.entity(bookHotelRequest, MediaType.APPLICATION_JSON));
//        handleHotelBookingResponse(hotelResponse, bookTripSagaData);
//    }
//
//    private void bookFlight() {
//        if (checkForFailures()) {
//            return;
//        }
//
//        logger.info("Trying to book a flight.");
//        WebTarget flightServiceTarget = flightServiceClient.target(flightServiceBaseUri);
//        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();
//        // setting context header since new thread does not know about the current LRA context
//        Response flightResponse =
//                flightServiceTarget.request().post(Entity.entity(bookFlightRequest, MediaType.APPLICATION_JSON));
//        handleFlightBookingResponse(flightResponse, bookTripSagaData);
//    }
//
//    private void confirmHotelBooking() {
//        if (checkForFailures()) {
//            return;
//        }
//
//        logger.info("Trying to confirm the hotel booking.");
//        String hotelConfirmUri = String.format("%s/%s/confirm", hotelServiceBaseUri,
//                bookTripSagaData.getTripId());
//        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelConfirmUri);
//        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
//                bookTripSagaData.getTripId());
//        // setting context header since new thread does not know about the current LRA context
//        Response confirmTripResponse =
//                hotelServiceTarget.request().put(Entity.entity(confirmHotelBooking, MediaType.APPLICATION_JSON_TYPE));
//        logger.info("Received from HotelService: " + confirmTripResponse.getStatus());
//    }
//
//    private void confirmTripBooking() {
//        if (checkForFailures()) {
//            return;
//        }
//
//        logger.info("Trying to confirm the trip.");
//        String travelConfirmUri = String.format("%s/trips/%s/confirm", travelServiceBaseUri,
//                bookTripSagaData.getTripId());
//        WebTarget travelServiceTarget = travelServiceClient.target(travelConfirmUri);
//        ConfirmTripBooking confirmTripBooking = new ConfirmTripBooking(bookTripSagaData.getTripId(),
//                bookTripSagaData.getHotelId(), bookTripSagaData.getFlightId());
//        // setting context header since new thread does not know about the current LRA context
//        Response confirmTripResponse =
//                travelServiceTarget.request().put(Entity.entity(confirmTripBooking, MediaType.APPLICATION_JSON));
//        logger.info("Received from TravelService: " + confirmTripResponse.getStatus());
//    }
//
//    // TODO refactor handle methods
//    private void handleHotelBookingResponse(final Response hotelResponse, final BookTripSagaData bookTripSagaData) {
//        if (hotelResponse.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()) {
//            BookHotelResponse bookHotelResponse = hotelResponse.readEntity(BookHotelResponse.class);
//            logger.info("Received from HotelService: " + bookHotelResponse);
//            long hotelId = bookHotelResponse == null ? -1 : bookHotelResponse.getBookingId();
//            bookTripSagaData.setHotelId(hotelId);
//        } else if (hotelResponse.getStatusInfo().getStatusCode() == Response.Status.INTERNAL_SERVER_ERROR
//        .getStatusCode()) {
//            JsonObject hotelResponseFailure = hotelResponse.readEntity(JsonObject.class);
//            Jsonb jsonb = JsonbBuilder.create();
//            try {
//                NoHotelAvailable noHotelAvailable = jsonb.fromJson(hotelResponseFailure.toString(),
//                        NoHotelAvailable.class);
//                bookTripSagaData.setRejectionReason(RejectionReason.NO_HOTEL_AVAILABLE);
//                logger.info("Received response NoHotelAvailable: " + noHotelAvailable);
//            } catch (JsonbException ignore) {
//                try {
//                    ErrorMessage errorMessage = jsonb.fromJson(hotelResponseFailure.toString(), ErrorMessage.class);
//                    logger.info("Received errorMessage: " + errorMessage);
//                } catch (JsonbException jsonbException) {
//                    logger.warning("Hotel answer could not be processed: " + jsonbException.getMessage());
//                }
//            }
//        } else {
//            logger.warning("Something went wrong when receiving hotel answer: " + hotelResponse.getStatusInfo()
//            .getStatusCode());
//        }
//    }
//
//    // TODO refactor handle methods
//    private void handleFlightBookingResponse(final Response flightResponse, final BookTripSagaData bookTripSagaData) {
//        if (flightResponse.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()) {
//            BookFlightResponse bookFlightResponse = flightResponse.readEntity(BookFlightResponse.class);
//            logger.info("Received from FlightService: " + bookFlightResponse);
//            long flightId = bookFlightResponse == null ? -1 : bookFlightResponse.getFlightBookingId();
//            bookTripSagaData.setFlightId(flightId);
//        } else if (flightResponse.getStatusInfo().getStatusCode() == Response.Status.INTERNAL_SERVER_ERROR
//        .getStatusCode()) {
//            JsonObject hotelResponseFailure = flightResponse.readEntity(JsonObject.class);
//            Jsonb jsonb = JsonbBuilder.create();
//            try {
//                NoFlightAvailable noFlightAvailable = jsonb.fromJson(hotelResponseFailure.toString(),
//                        NoFlightAvailable.class);
//                bookTripSagaData.setRejectionReason(RejectionReason.NO_FLIGHT_AVAILABLE);
//                logger.info("Received response NoFlightAvailable: " + noFlightAvailable);
//            } catch (JsonbException ignore) {
//                try {
//                    ErrorMessage errorMessage = jsonb.fromJson(hotelResponseFailure.toString(), ErrorMessage.class);
//                    logger.info("Received errorMessage: " + errorMessage);
//                } catch (JsonbException jsonbException) {
//                    logger.warning("Flight answer could not be processed: " + jsonbException.getMessage());
//                }
//            }
//        } else {
//            logger.warning("Something went wrong when receiving flight answer: " + flightResponse.getStatusInfo()
//            .getStatusCode());
//        }
//    }
//
//    private boolean checkForFailures() {
//        return bookTripSagaData.getRejectionReason() != null;
//    }
}
