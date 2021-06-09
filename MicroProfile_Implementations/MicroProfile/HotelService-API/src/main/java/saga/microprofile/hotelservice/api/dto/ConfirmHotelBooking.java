package saga.microprofile.hotelservice.api.dto;

public class ConfirmHotelBooking {

    private final long tripId;

    private final long bookingId;

    // default constructor necessary for Eventuate Framework
    private ConfirmHotelBooking() {
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

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "ConfirmHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
