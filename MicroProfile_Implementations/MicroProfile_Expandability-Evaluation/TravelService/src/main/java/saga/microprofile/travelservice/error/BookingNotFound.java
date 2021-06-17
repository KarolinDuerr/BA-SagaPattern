package saga.microprofile.travelservice.error;

import java.net.URI;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(final long bookingId) {
        super(String.format("Trip booking (ID: %d) could not be found.", bookingId));
    }

    public BookingNotFound(final URI lraId) {
        super(String.format("Trip booking associated with LRA (ID: %d) could not be found.", lraId.toString()));
    }
}
