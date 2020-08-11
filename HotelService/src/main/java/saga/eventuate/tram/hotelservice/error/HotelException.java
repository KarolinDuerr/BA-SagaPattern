package saga.eventuate.tram.hotelservice.error;

public class HotelException extends Exception {

    private ErrorType errorType;

    public HotelException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public HotelException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }
}
