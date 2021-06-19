package saga.microprofile.travelservice.api.dto;

public class ConfirmTripBooking {

    private long tripId;

    private long hotelId;

    private long flightId;

    public ConfirmTripBooking() {
        tripId = -1;
        hotelId = -1;
        flightId = -1;
    }

    public ConfirmTripBooking(final long tripId, final long hotelId, final long flightId) {
        this.tripId = tripId;
        this.hotelId = hotelId;
        this.flightId = flightId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(final long hotelId) {
        this.hotelId = hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(final long flightId) {
        this.flightId = flightId;
    }

    @Override
    public String toString() {
        return "ConfirmTripBooking{" +
                "tripId=" + tripId +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                '}';
    }
}
