package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class RebookHotelRequest implements Command {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private RebookHotelRequest() {
        tripId = -1;
        bookingId = -1;
    }

    public RebookHotelRequest(final long bookingId, final long tripId) {
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
        return "RebookHotelRequest{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
