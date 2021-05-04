package saga.microprofile.flightservice.api.dto;

public class BookFlightResponse {

    private long tripId;

    private long flightBookingId;

    private String bookingStatus;

    private BookFlightResponse() {

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

    public long getFlightBookingId() {
        return flightBookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "BookFlightResponse{" +
                "flightBookingId=" + flightBookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
