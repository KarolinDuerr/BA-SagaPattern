package saga.microprofile.flightservice.error;

public class UnsupportedStateTransition extends RuntimeException {

    public UnsupportedStateTransition(final String message) {
        super(message);
    }
}
