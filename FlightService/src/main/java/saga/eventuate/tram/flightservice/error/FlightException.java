package saga.eventuate.tram.flightservice.error;

public class FlightException extends FlightServiceException {

    public FlightException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public FlightException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
