package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;
import saga.eventuate.tram.travelservice.model.RejectionReason;

public class RejectTripCommand implements Command {

    private final long tripId;

    private final RejectionReason rejectionReason;

    public RejectTripCommand() {
        tripId = -1;
        rejectionReason = RejectionReason.UNKNOWN;
    }

    public RejectTripCommand(final long tripId, final RejectionReason rejectionReason) {
        this.tripId = tripId;
        this.rejectionReason = rejectionReason;
    }

    public long getTripId() {
        return tripId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }
}
