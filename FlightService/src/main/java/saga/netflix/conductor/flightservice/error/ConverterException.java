package saga.netflix.conductor.flightservice.error;

public class ConverterException extends FlightServiceException {


    public ConverterException(String message) {
        super(ErrorType.INVALID_PARAMETER, message);
    }

    public ConverterException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public ConverterException(String message, Throwable cause) {
        super(ErrorType.INVALID_PARAMETER, message, cause);
    }

    public ConverterException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
