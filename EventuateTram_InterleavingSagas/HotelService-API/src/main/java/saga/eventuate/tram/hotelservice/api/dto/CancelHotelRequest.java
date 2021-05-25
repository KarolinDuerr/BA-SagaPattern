package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class CancelHotelRequest implements Command {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private CancelHotelRequest() {
        bookingId = -1;
        tripId = -1;
    }

    public CancelHotelRequest(final long bookingId, final long tripId) {
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
        return "CancelHotelRequest{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
