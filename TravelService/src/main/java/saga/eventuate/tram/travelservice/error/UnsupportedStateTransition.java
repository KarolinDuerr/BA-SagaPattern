package saga.eventuate.tram.travelservice.error;

public class UnsupportedStateTransition extends TravelException {

    public UnsupportedStateTransition(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public UnsupportedStateTransition(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
