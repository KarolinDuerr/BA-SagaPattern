package saga.eventuate.tram.flightservice.api.dto;

public class CancelFlightResponse {

    private long flightBookingId;

    private String bookingStatus;

    private CancelFlightResponse() {

    }

    public CancelFlightResponse(final long flightBookingId, final String bookingStatus) {
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
        return "CancelFlightResponse{" +
                "flightBookingId=" + flightBookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
