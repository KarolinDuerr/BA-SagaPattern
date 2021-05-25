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

    private CancelTripRequest() {

    }

    private CancelTripRequest(final long tripId, final long customerId) {
        this.tripId = tripId;
        this.customerId = customerId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CancelTripRequest{" +
                "tripId=" + tripId +
                ", customerId=" + customerId +
                '}';
    }
}
