package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class CancelHotelBooking implements Command {

    private final long bookingId;

    public CancelHotelBooking() {
        bookingId = -1;
    }

    public CancelHotelBooking(final long bookingId) {
        this.bookingId = bookingId;
    }

    public long getBookingId() {
        return bookingId;
    }
}
