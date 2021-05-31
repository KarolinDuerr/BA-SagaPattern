package saga.eventuate.tram.flightservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class CancelFlightRequest implements Command {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private CancelFlightRequest() {
        tripId = -1;
        bookingId = -1;
    }

    public CancelFlightRequest(final long bookingId, final long tripId) {
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
        return "CancelFlightRequest{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
