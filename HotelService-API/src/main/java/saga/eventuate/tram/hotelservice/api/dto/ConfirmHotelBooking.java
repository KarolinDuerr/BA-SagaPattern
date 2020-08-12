package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelBooking implements Command {

    private long bookingId;

    public ConfirmHotelBooking() {

    }

    public ConfirmHotelBooking(final long bookingId) {
        this.bookingId = bookingId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }
}
