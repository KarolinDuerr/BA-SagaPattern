package saga.netflix.conductor.travelservice.model.dto;

import saga.netflix.conductor.travelservice.model.RejectionReason;

public class RejectTripBooking {

    private final long tripId;

    private final RejectionReason rejectionReason;

    private RejectTripBooking() {
        tripId = -1;
        rejectionReason = RejectionReason.REASON_UNKNOWN;
    }

    public RejectTripBooking(final long tripId, final RejectionReason rejectionReason) {
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
        return "RejectTripCommand{" +
                "tripId=" + tripId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
