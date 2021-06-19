package saga.microprofile.travelservice.saga;

import saga.microprofile.flightservice.api.dto.BookFlightResponse;
import saga.microprofile.flightservice.api.dto.NoFlightAvailable;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.NoHotelAvailable;
import saga.microprofile.travelservice.api.dto.RejectionReason;
import saga.microprofile.travelservice.controller.ILraCoordinatorService;
import saga.microprofile.travelservice.error.ErrorMessage;
import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelCallbackException;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class BookingCallback<T> implements InvocationCallback<Response> {

    private static final Logger logger = Logger.getLogger(BookingCallback.class.toString());

    private final ILraCoordinatorService lraCoordinatorService;

    private final BookTripSagaData bookTripSagaData;

    private final CountDownLatch latch;

    private final Class<T> type;

    public BookingCallback(final BookTripSagaData bookTripSagaData, final CountDownLatch latch, final Class<T> type,
                           final ILraCoordinatorService lraCoordinatorService) {
        this.bookTripSagaData = bookTripSagaData;
        this.latch = latch;
        this.type = type;
        this.lraCoordinatorService = lraCoordinatorService;
    }

    @Override
    public void completed(Response response) {
        String message = String.format("Callback for %s completed with status: %s.", type.getTypeName(),
                response.getStatus());
        logger.info(message);
        chooseHandlerMethod(response);
        latch.countDown();
    }

    @Override
    public void failed(Throwable throwable) {
        String message = String.format("Callback for %s failed with: %s.", type.getTypeName(),
                Arrays.toString(throwable.getStackTrace()));
        logger.info(message);
        lraCoordinatorService.cancelActiveLra(bookTripSagaData.getTripInformation().getLraId());
        latch.countDown();
    }

    private void chooseHandlerMethod(final Response response) {
        if (Objects.equals(type.getTypeName(), BookHotelResponse.class.getTypeName())) {
            handleHotelBookingResponse(response);
        } else if (Objects.equals(type.getTypeName(), BookFlightResponse.class.getTypeName())) {
            handleFlightBookingResponse(response);
        } else {
            String message = String.format("Could not find class for type (%s) and received response: %s",
                    type.getTypeName(), response.getStatus());
            logger.info(message);
            bookTripSagaData.setRejectionReason(RejectionReason.REASON_UNKNOWN);
            throw new TravelCallbackException(ErrorType.INTERNAL_ERROR,
                    "Booking response could not be processed.", response.readEntity(JsonObject.class));
        }
    }

    // TODO refactor handle methods
    private void handleHotelBookingResponse(final Response hotelResponse) {
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
    private void handleFlightBookingResponse(final Response flightResponse) {
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


    // TODO
    private void test(final Response response, final Class<T> type) {
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
}
