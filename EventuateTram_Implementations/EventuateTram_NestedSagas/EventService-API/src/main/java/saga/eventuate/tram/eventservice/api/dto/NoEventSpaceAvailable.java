package saga.eventuate.tram.eventservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class NoEventSpaceAvailable implements Command {

    private final long hotelBookingId;

    // default constructor necessary for Eventuate Framework
    private NoEventSpaceAvailable() {
        hotelBookingId = -1;
    }

    public NoEventSpaceAvailable(final long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    @Override
    public String toString() {
        return "NoEventSpaceAvailable{" +
                "hotelBookingId=" + hotelBookingId +
                '}';
    }
}
