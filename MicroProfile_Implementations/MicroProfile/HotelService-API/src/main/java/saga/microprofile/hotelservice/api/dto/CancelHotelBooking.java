package saga.microprofile.hotelservice.api.dto;

public class CancelHotelBooking {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private CancelHotelBooking() {
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

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "CancelHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
