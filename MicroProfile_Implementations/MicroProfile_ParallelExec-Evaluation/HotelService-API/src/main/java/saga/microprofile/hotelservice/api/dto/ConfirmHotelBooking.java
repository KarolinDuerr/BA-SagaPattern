package saga.microprofile.hotelservice.api.dto;

public class ConfirmHotelBooking {

    private long tripId;

    private long bookingId;

    // default constructor necessary for unmarshalling
    public ConfirmHotelBooking() {
        tripId = -1;
        bookingId = -1;
    }

    public ConfirmHotelBooking(final long bookingId, final long tripId) {
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
        return "ConfirmHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
