package saga.eventuate.tram.hotelservice.command;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelEventBooking implements Command {

    private final long hotelBookingId;

    private final long eventBookingId;

    private ConfirmHotelEventBooking() {
        hotelBookingId = -1;
        eventBookingId = -1;
    }

    public ConfirmHotelEventBooking(final long hotelBookingId, final long eventBookingId) {
        this.hotelBookingId = hotelBookingId;
        this.eventBookingId = eventBookingId;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    @Override
    public String toString() {
        return "ConfirmHotelEventBooking{" +
                "hotelBookingId=" + hotelBookingId +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
