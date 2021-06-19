package saga.microprofile.travelservice.saga;

import saga.microprofile.hotelservice.api.dto.ConfirmHotelBooking;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.controller.ILraCoordinatorService;
import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelCallbackException;

import javax.json.JsonObject;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

// TODO test if can be used
public class ConfirmationCallback<T> implements InvocationCallback<Response> {

    private static final Logger logger = Logger.getLogger(ConfirmationCallback.class.toString());

    private final ILraCoordinatorService lraCoordinatorService;

    private final CountDownLatch latch;

    private final String lraId;

    private final Class<T> type;

    public ConfirmationCallback(final CountDownLatch latch, final Class<T> type, final String lraId,
                                final ILraCoordinatorService lraCoordinatorService) {
        this.latch = latch;
        this.type = type;
        this.lraId = lraId;
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
        lraCoordinatorService.cancelActiveLra(lraId);
        latch.countDown();
    }

    private void chooseHandlerMethod(final Response response) {
        if (Objects.equals(type.getTypeName(), ConfirmHotelBooking.class.getTypeName())) {
            logger.info("Received confirmation answer from HotelService: " + response.getStatus());
        } else if (Objects.equals(type.getTypeName(), ConfirmTripBooking.class.getTypeName())) {
            logger.info("Received confirmation answer from TravelService: " + response.getStatus());
        } else {
            String message = String.format("Could not find class for type (%s) and received response: %s",
                    type.getTypeName(), response.getStatus());
            logger.info(message);
            throw new TravelCallbackException(ErrorType.INTERNAL_ERROR,
                    "Booking response could not be processed.", response.readEntity(JsonObject.class));
        }
    }
}
