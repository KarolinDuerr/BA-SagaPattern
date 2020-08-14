package saga.eventuate.tram.flightservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class NoFlightAvailable implements Command {

    private final long tripId;

    private NoFlightAvailable() {
        tripId = -1;
    }

    public NoFlightAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }
}
