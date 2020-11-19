package saga.eventuate.tram.flightservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class CancelFlightBooking implements Command {

    private final long tripId;

    private final long bookingId;

    private CancelFlightBooking() {
        bookingId = -1;
        tripId = -1;
    }

    public CancelFlightBooking(final long bookingId, final long tripId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "CancelFlightBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
