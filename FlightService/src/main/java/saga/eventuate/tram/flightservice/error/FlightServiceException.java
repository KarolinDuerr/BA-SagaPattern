package saga.eventuate.tram.flightservice.error;

/**
 * This is the general exception for the flight service, if there are any issues
 * faced, during using the flight service.
 *
 * @see Exception
 */
public class FlightServiceException extends Exception {

    /**
     * The {@link ErrorType} that occurred and lead to this exception.
     */
    private final ErrorType errorType;

    public FlightServiceException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public FlightServiceException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", errorType, getMessage());
    }
}
