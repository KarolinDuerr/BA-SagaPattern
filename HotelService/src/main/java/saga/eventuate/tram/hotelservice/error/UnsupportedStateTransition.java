package saga.eventuate.tram.hotelservice.error;

public class UnsupportedStateTransition extends RuntimeException {

    public UnsupportedStateTransition(String message) {
        super(message);
    }
}
