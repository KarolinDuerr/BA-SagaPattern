package saga.eventuate.tram.travelservice.api.dto;

public class CancelTripResponse {

    private final long tripId;

    private String bookingStatus;

    private CancelTripResponse() {
        tripId = -1;
    }

    public CancelTripResponse(final Long tripId, final String bookingStatus) {
        this.tripId = tripId;
        this.bookingStatus = bookingStatus;
    }

    public long getTripId() {
        return tripId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "BookTripResponse{" +
                "tripId=" + tripId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
