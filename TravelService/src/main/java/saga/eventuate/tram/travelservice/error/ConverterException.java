package saga.eventuate.tram.travelservice.error;

public class ConverterException extends Exception {

    private ErrorType errorType;

    public ConverterException(String message) {
        super(message);
        errorType = ErrorType.INVALID_PARAMETER;
    }

    public ConverterException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
        errorType = ErrorType.INVALID_PARAMETER;
    }

    public ConverterException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

}
