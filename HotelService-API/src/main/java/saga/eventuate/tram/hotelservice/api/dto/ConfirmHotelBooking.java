package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelBooking implements Command {

    private final long bookingId;

    public ConfirmHotelBooking() {
        bookingId = -1;
    }

    public ConfirmHotelBooking(final long bookingId) {
        this.bookingId = bookingId;
    }

    public long getBookingId() {
        return bookingId;
    }
}
