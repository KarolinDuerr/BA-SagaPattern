package saga.eventuate.tram.travelservice.error;

public class TravelException extends TravelServiceException {

    public TravelException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public TravelException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
