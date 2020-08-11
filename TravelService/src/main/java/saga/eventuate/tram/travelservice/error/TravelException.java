package saga.eventuate.tram.travelservice.error;

public class TravelException extends Exception {

    private ErrorType errorType;

    public TravelException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public TravelException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }
}
