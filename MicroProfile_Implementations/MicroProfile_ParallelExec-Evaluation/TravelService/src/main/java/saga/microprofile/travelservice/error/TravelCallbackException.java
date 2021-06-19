package saga.microprofile.travelservice.error;

import javax.json.JsonObject;

public class TravelCallbackException extends RuntimeException {

    /**
     * The {@link ErrorType} that occurred and lead to this exception.
     */
    private final ErrorType errorType;

    private final JsonObject jsonObject;

    public TravelCallbackException(final ErrorType errorType, final String message, final JsonObject jsonObject) {
        super(message);
        this.errorType = errorType;
        this.jsonObject = jsonObject;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%s: %s\n--> %s", errorType, getMessage(), getJsonObject());
    }
}
