package saga.eventuate.tram.travelservice.error;

public class ConverterException extends TravelServiceException {

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
