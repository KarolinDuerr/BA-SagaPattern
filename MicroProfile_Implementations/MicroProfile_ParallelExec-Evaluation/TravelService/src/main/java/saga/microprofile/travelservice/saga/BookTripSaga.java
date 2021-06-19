package saga.microprofile.travelservice.saga;

import saga.microprofile.flightservice.api.dto.BookFlightRequest;
import saga.microprofile.flightservice.api.dto.BookFlightResponse;
import saga.microprofile.hotelservice.api.dto.BookHotelRequest;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.ConfirmHotelBooking;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.controller.ILraCoordinatorService;

import javax.annotation.PreDestroy;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

public class BookTripSaga implements Runnable {

    private static final Logger logger = Logger.getLogger(BookTripSaga.class.toString());

    private final ILraCoordinatorService lraCoordinatorService;

    private final BookTripSagaData bookTripSagaData;

    private final Client hotelServiceClient;

    private final Client flightServiceClient;

    private final Client travelServiceClient;

    private final String flightServiceBaseUri;

    private final String hotelServiceBaseUri;

    private final String travelServiceBaseUri;

    private final String lraId;

    private final CountDownLatch bookingLatch = new CountDownLatch(2);

    public BookTripSaga(final BookTripSagaData bookTripSagaData, final String flightServiceUri,
                        final String hotelServiceUri, final String travelServiceUri, final String lraId,
                        final ILraCoordinatorService lraCoordinatorService) {
        this.lraCoordinatorService = lraCoordinatorService;
        this.bookTripSagaData = bookTripSagaData;
        this.lraId = lraId;

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
        bookFlight();

        try {
            // Wait for the booking requests to finish
            bookingLatch.await();
        } catch (InterruptedException ex) {
            logger.info("Exception: " + ex.getMessage()); // TODO cancel LRA?
        }

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
        BookingCallback<BookHotelResponse> hotelBookingCallback = new BookingCallback<>(bookTripSagaData, bookingLatch,
                BookHotelResponse.class, lraCoordinatorService);
        // setting context header since new thread does not know about the current LRA context
        hotelServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).async().post(Entity.entity(bookHotelRequest, MediaType.APPLICATION_JSON), hotelBookingCallback);
    }

    private void bookFlight() {
        if (checkForFailures()) {
            return;
        }

        logger.info("Trying to book a flight.");
        WebTarget flightServiceTarget = flightServiceClient.target(flightServiceBaseUri);
        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();
        BookingCallback<BookFlightResponse> flightBookingCallback = new BookingCallback<>(bookTripSagaData,
                bookingLatch, BookFlightResponse.class, lraCoordinatorService);
        // setting context header since new thread does not know about the current LRA context
        flightServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).async().post(Entity.entity(bookFlightRequest, MediaType.APPLICATION_JSON), flightBookingCallback);
    }

    private void confirmHotelBooking() {
        logger.info("Trying to confirm the hotel booking.");
        String hotelConfirmUri = String.format("%s/%s/confirm", hotelServiceBaseUri, bookTripSagaData.getTripId());
        WebTarget hotelServiceTarget = hotelServiceClient.target(hotelConfirmUri);
        ConfirmHotelBooking confirmHotelBooking = new ConfirmHotelBooking(bookTripSagaData.getHotelId(),
                bookTripSagaData.getTripId());
        // setting context header since new thread does not know about the current LRA context
        Response confirmHotelResponse =
                hotelServiceTarget.request().header(LRA_HTTP_CONTEXT_HEADER, lraId).put(Entity.entity(confirmHotelBooking, MediaType.APPLICATION_JSON_TYPE));
        logger.info("Received confirmation answer from HotelService: " + confirmHotelResponse.getStatus());
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
        logger.info("Received confirmation answer from TravelService: " + confirmTripResponse.getStatus());
    }

    private boolean checkForFailures() {
        return bookTripSagaData.getRejectionReason() != null;
    }

    @PreDestroy
    public void cleanUp() {
        logger.info("Clean-up: Closing the clients.");
        hotelServiceClient.close();
        flightServiceClient.close();
        travelServiceClient.close();
    }
}
