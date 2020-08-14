package saga.eventuate.tram.hotelservice.error;

public class HotelException extends HotelServiceException {

    public HotelException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public HotelException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
