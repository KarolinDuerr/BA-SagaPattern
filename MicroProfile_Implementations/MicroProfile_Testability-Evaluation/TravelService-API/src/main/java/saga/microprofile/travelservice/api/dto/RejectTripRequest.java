package saga.microprofile.travelservice.api.dto;

public class RejectTripRequest {

    private long tripId;

    private RejectionReason rejectionReason;

    public RejectTripRequest() {
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

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "RejectTripRequest{" +
                "tripId=" + tripId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
