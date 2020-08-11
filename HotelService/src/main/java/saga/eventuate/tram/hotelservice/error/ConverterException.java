package saga.eventuate.tram.hotelservice.error;

public class ConverterException extends Exception {

    private ErrorType errorType = ErrorType.INVALID_PARAMETER;

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

}
