package saga.eventuate.tram.travelservice.saga;

import saga.eventuate.tram.travelservice.model.RejectionReason;

public class BookTripSagaData {

    private final long tripId;

    private RejectionReason rejectionReason;

    public BookTripSagaData() {
        this.tripId = -1;
    }

    public BookTripSagaData(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
