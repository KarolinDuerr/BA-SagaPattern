package saga.eventuate.tram.eventservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class CancelEventBooking implements Command {

    private final long hotelBookingId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private CancelEventBooking() {
        bookingId = -1;
        hotelBookingId = -1;
    }

    public CancelEventBooking(final long bookingId, final long hotelBookingId) {
        this.bookingId = bookingId;
        this.hotelBookingId = hotelBookingId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    @Override
    public String toString() {
        return "CancelEventBooking{" +
                "hotelBookingId=" + hotelBookingId +
                ", bookingId=" + bookingId +
                '}';
    }
}
