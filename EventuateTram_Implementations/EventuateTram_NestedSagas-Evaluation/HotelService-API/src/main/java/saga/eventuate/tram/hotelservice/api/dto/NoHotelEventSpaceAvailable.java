package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class NoHotelEventSpaceAvailable implements Command {

    private final long tripId;

    // default constructor necessary for Eventuate Framework
    private NoHotelEventSpaceAvailable() {
        tripId = -1;
    }

    public NoHotelEventSpaceAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "NoHotelEventSpaceAvailable{" +
                "tripId=" + tripId +
                '}';
    }
}
