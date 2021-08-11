package saga.microprofile.travelservice.api.dto;

import java.io.Serializable;

public class BookTripResponse implements Serializable {

    private long tripId;

    private String bookingStatus;

    public BookTripResponse() {
        tripId = -1;
    }

    public BookTripResponse(final Long tripId, final String bookingStatus) {
        this.tripId = tripId;
        this.bookingStatus = bookingStatus;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "BookTripResponse{" +
                "tripId=" + tripId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
