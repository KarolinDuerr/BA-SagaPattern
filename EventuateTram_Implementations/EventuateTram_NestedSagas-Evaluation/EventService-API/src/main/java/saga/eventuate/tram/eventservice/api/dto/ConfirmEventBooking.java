package saga.eventuate.tram.eventservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmEventBooking implements Command {

    private final long hotelBookingId;

    private final long eventBookingId;

    // default constructor necessary for Eventuate Framework
    private ConfirmEventBooking() {
        hotelBookingId = -1;
        eventBookingId = -1;
    }

    public ConfirmEventBooking(final long eventBookingId, final long hotelBookingId) {
        this.eventBookingId = eventBookingId;
        this.hotelBookingId = hotelBookingId;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    @Override
    public String toString() {
        return "ConfirmEventBooking{" +
                "hotelBookingId=" + hotelBookingId +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
