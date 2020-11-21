package saga.eventuate.tram.flightservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Flight booking could not be found.")
public class BookingNotFound extends RuntimeException {

    public BookingNotFound(final long bookingId) {
        super(String.format("Flight booking (ID: %d) could not be found.", bookingId));
    }
}
