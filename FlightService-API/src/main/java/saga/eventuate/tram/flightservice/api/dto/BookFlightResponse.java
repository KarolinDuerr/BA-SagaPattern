package saga.eventuate.tram.flightservice.api.dto;

public class BookFlightResponse {

    private long flightBookingId;

    private String bookingStatus;

    public BookFlightResponse() {

    }

    public BookFlightResponse(final long flightBookingId, final String bookingStatus) {
        this.flightBookingId = flightBookingId;
        this.bookingStatus = bookingStatus;
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
