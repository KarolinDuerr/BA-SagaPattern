package saga.eventuate.tram.flightservice.error;

public class ErrorMessage {

    private ErrorType errorType;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(final ErrorType errorType, final String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public void setErrorType(final ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorType=" + errorType +
                ", message='" + message + '\'' +
                '}';
    }
}
