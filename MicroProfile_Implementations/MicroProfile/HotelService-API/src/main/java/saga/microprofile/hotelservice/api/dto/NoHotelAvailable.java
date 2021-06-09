package saga.microprofile.hotelservice.api.dto;

import java.io.Serializable;

public class NoHotelAvailable implements Serializable {

    private final long tripId;

    // default constructor necessary for Eventuate Framework
    private NoHotelAvailable() {
        tripId = -1;
    }

    public NoHotelAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "NoHotelAvailable{" +
                "tripId=" + tripId +
                '}';
    }
}
