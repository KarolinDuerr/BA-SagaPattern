package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelBooking implements Command {

    private final long tripId;

    private final long bookingId;

    public ConfirmHotelBooking() {
        bookingId = -1;
        tripId = -1;
    }

    public ConfirmHotelBooking(final long bookingId, final long tripId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getTripId() {
        return tripId;
    }
}
