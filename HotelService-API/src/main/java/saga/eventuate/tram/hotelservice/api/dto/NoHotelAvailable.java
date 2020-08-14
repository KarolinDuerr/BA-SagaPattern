package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class NoHotelAvailable implements Command {

    private final long tripId;

    public NoHotelAvailable() {
        tripId = -1;
    }

    public NoHotelAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "NoHotelAvailable{" +
                "tripId=" + tripId +
                '}';
    }
}
