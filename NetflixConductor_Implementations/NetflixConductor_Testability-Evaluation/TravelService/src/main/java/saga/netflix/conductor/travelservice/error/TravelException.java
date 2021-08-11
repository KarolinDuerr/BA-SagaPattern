package saga.netflix.conductor.travelservice.error;

public class TravelException extends TravelServiceException {

    public TravelException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public TravelException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
