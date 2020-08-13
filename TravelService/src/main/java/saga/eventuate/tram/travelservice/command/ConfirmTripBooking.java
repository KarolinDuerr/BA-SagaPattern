package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;

public class ConfirmTripBooking implements Command {

    private final long tripId;

    public ConfirmTripBooking() {
        tripId = -1;
    }

    public ConfirmTripBooking(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }
}
