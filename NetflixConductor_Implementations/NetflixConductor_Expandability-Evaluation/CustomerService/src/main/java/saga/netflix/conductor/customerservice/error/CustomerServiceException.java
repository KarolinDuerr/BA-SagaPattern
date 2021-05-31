package saga.netflix.conductor.customerservice.error;

/**
 * This is the general exception for the flight service, if there are any issues
 * faced, during using the flight service.
 *
 * @see Exception
 */
public class CustomerServiceException extends Exception {

    /**
     * The {@link ErrorType} that occurred and lead to this exception.
     */
    private final ErrorType errorType;

    public CustomerServiceException(final ErrorType errorType, final String message) {
        super(message);
        this.errorType = errorType;
    }

    public CustomerServiceException(final ErrorType errorType, final String message, final Throwable cause) {
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
