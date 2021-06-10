package saga.microprofile.hotelservice.api.dto;

import java.io.Serializable;

public class NoHotelAvailable implements Serializable {

    private long tripId;

    // default constructor necessary
    public NoHotelAvailable() {
        tripId = -1;
    }

    public NoHotelAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "NoHotelAvailable{" +
                "tripId=" + tripId +
                '}';
    }
}
