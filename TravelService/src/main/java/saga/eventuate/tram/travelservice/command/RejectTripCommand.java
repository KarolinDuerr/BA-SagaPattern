package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;

public class RejectTripCommand implements Command {

    private final long tripId;

    public RejectTripCommand() {
        tripId = -1;
    }

    public RejectTripCommand(long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }
}
