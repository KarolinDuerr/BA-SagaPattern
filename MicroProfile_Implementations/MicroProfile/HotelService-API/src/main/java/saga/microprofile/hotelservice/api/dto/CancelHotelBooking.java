package saga.microprofile.hotelservice.api.dto;

public class CancelHotelBooking {

    private long tripId;

    private long bookingId;

    // default constructor necessary for unmarshalling
    public CancelHotelBooking() {
        bookingId = -1;
        tripId = -1;
    }

    public CancelHotelBooking(final long bookingId, final long tripId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(final long bookingId) {
        this.bookingId = bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "CancelHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
