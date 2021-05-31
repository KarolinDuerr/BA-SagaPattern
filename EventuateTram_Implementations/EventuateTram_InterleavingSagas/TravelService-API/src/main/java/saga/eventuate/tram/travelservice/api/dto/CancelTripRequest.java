package saga.eventuate.tram.travelservice.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CancelTripRequest {

    @NotNull
    @NotBlank
    private long tripId;

    @NotNull
    @NotBlank
    private long customerId;

    @NotNull
    @NotBlank
    private FailureType provokeFailureType;

    private CancelTripRequest() {

    }

    private CancelTripRequest(final long tripId, final long customerId, final FailureType provokeFailureType) {
        this.tripId = tripId;
        this.customerId = customerId;
        this.provokeFailureType = provokeFailureType;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final long customerId) {
        this.customerId = customerId;
    }

    public FailureType getProvokeFailureType() {
        return provokeFailureType;
    }

    public void setProvokeFailureType(final FailureType provokeFailureType) {
        this.provokeFailureType = provokeFailureType;
    }

    @Override
    public String toString() {
        return "CancelTripRequest{" +
                "tripId=" + tripId +
                ", customerId=" + customerId +
                ", provokeFailure=" + provokeFailureType +
                '}';
    }
}
