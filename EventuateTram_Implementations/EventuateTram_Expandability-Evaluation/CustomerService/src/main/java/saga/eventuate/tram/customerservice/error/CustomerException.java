package saga.eventuate.tram.customerservice.error;

public class CustomerException extends CustomerServiceException {

    public CustomerException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public CustomerException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
