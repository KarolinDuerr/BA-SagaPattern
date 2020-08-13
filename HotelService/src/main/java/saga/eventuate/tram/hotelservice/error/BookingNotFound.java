package saga.eventuate.tram.hotelservice.error;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(final long bookingId) {
        super(String.format("Hotel booking (ID: %d) could not be found.", bookingId));
    }
}
