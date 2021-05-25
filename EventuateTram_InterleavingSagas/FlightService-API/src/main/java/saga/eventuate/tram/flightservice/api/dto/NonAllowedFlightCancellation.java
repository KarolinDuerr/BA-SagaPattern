package saga.eventuate.tram.flightservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class NonAllowedFlightCancellation implements Command {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private NonAllowedFlightCancellation() {
        tripId = -1;
        bookingId = -1;
    }

    public NonAllowedFlightCancellation(final long bookingId, final long tripId) {
        this.tripId = tripId;
        this.bookingId = bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    @Override
    public String toString() {
        return "NonAllowedCancellation{" +
                "tripId=" + tripId +
                "bookingId=" + bookingId +
                '}';
    }
}
