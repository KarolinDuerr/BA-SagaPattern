package saga.netflix.conductor.flightservice.error;

public class FlightException extends FlightServiceException {

    public FlightException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public FlightException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
