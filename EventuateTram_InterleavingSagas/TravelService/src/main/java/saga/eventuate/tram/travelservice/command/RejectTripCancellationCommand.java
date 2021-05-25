package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;
import saga.eventuate.tram.travelservice.model.RejectionReason;

public class RejectTripCancellationCommand implements Command {

    private final long tripId;

    private final RejectionReason rejectionReason;

    private RejectTripCancellationCommand() {
        tripId = -1;
        rejectionReason = RejectionReason.REASON_UNKNOWN;
    }

    public RejectTripCancellationCommand(final long tripId, final RejectionReason rejectionReason) {
        this.tripId = tripId;
        this.rejectionReason = rejectionReason;
    }

    public long getTripId() {
        return tripId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public String toString() {
        return "RejectTripCancellationCommand{" +
                "tripId=" + tripId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
