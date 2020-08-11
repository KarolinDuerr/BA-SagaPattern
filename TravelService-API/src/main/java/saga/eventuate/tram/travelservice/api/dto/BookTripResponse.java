package saga.eventuate.tram.travelservice.api.dto;

public class BookTripResponse {

    private long tripId;

//    private String hotelName; // TODO

//    private String // TODO

    private String bookingStatus;

    public BookTripResponse(final Long tripId, final String bookingStatus) {
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
