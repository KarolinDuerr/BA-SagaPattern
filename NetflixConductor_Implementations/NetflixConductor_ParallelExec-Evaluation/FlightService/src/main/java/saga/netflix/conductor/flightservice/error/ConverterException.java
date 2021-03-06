package saga.netflix.conductor.flightservice.error;

public class ConverterException extends FlightServiceException {


    public ConverterException(final String message) {
        super(ErrorType.INVALID_PARAMETER, message);
    }

    public ConverterException(final ErrorType errorType, final String message) {
        super(errorType, message);
    }

    public ConverterException(final String message, final Throwable cause) {
        super(ErrorType.INVALID_PARAMETER, message, cause);
    }

    public ConverterException(final ErrorType errorType, final String message, final Throwable cause) {
        super(errorType, message, cause);
    }
}
