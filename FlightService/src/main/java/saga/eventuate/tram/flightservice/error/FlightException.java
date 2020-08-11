package saga.eventuate.tram.flightservice.error;

public class FlightException extends Exception {

    private ErrorType errorType;

    public FlightException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public FlightException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }
}
