package saga.eventuate.tram.eventservice.error;

public class EventException extends EventServiceException {

    public EventException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public EventException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
