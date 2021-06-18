package saga.microprofile.hotelservice.error;

import java.net.URI;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(final long bookingId) {
        super(String.format("Hotel booking (ID: %d) could not be found.", bookingId));
    }

    public BookingNotFound(final URI lraId) {
        super(String.format("Hotel booking associated with LRA (ID: %d) could not be found.", lraId.toString()));
    }
}
