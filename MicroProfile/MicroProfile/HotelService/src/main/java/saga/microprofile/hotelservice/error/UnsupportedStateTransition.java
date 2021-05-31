package saga.microprofile.hotelservice.error;

public class UnsupportedStateTransition extends RuntimeException {

    public UnsupportedStateTransition(final String message) {
        super(message);
    }
}
