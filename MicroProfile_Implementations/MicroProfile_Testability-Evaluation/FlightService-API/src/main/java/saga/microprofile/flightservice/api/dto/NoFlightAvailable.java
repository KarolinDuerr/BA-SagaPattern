package saga.microprofile.flightservice.api.dto;

public class NoFlightAvailable {

    private long tripId;

    // default constructor necessary for unmarshalling
    public NoFlightAvailable() {
        tripId = -1;
    }

    public NoFlightAvailable(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "NoFlightAvailable{" +
                "tripId=" + tripId +
                '}';
    }
}
