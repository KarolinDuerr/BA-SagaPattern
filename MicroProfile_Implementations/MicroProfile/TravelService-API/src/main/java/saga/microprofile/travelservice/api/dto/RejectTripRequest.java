package saga.microprofile.travelservice.api.dto;

public class RejectTripRequest {

    private final long tripId;

    private final RejectionReason rejectionReason;

    private RejectTripRequest() {
        tripId = -1;
        rejectionReason = RejectionReason.REASON_UNKNOWN;
    }

    public RejectTripRequest(final long tripId, final RejectionReason rejectionReason) {
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
        return "RejectTripRequest{" +
                "tripId=" + tripId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
