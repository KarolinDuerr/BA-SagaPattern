package saga.microprofile.travelservice.error;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(final long bookingId) {
        super(String.format("Trip booking (ID: %d) could not be found.", bookingId));
    }
}
