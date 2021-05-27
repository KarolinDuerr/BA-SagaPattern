package saga.netflix.conductor.travelservice.saga.cancelTripSaga;

import saga.netflix.conductor.travelservice.model.RejectionReason;

public class CancelTripSagaData {

    private final long tripId;

    private final long hotelId;

    private final long flightId;

    private final long customerId;

    private RejectionReason rejectionReason;

    private CancelTripSagaData() {
        this.tripId = 0;
        this.hotelId = -1;
        this.flightId = -1;
        this.customerId = -1;
    }

    public CancelTripSagaData(final long tripId, final long hotelId, final long flightId, final long customerId) {
        this.tripId = tripId;
        this.hotelId = hotelId;
        this.flightId = flightId;
        this.customerId = customerId;
    }

    public long getTripId() {
        return tripId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "CancelTripSagaData{" +
                "tripId=" + tripId +
                ", customerId=" + customerId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
