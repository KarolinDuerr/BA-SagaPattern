package saga.microprofile.flightservice.api.dto;

public class BookFlightResponse {

    private long tripId;

    private long flightBookingId;

    private String bookingStatus;

    public BookFlightResponse() {

    }

    public BookFlightResponse(final long flightBookingId, final String bookingStatus) {
        this.tripId = -1;
        this.flightBookingId = flightBookingId;
        this.bookingStatus = bookingStatus;
    }

    public BookFlightResponse(final long tripId, final long flightBookingId, final String bookingStatus) {
        this.tripId = tripId;
        this.flightBookingId = flightBookingId;
        this.bookingStatus = bookingStatus;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(final long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "BookFlightResponse{" +
                "flightBookingId=" + flightBookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
