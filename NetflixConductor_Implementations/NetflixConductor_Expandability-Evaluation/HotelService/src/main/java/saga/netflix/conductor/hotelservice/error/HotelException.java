package saga.netflix.conductor.hotelservice.error;

public class HotelException extends HotelServiceException {

    public HotelException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public HotelException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
