package saga.eventuate.tram.travelservice.error;

public class UnsupportedStateTransition extends RuntimeException {

    public UnsupportedStateTransition(String message) {
        super(message);
    }
}
