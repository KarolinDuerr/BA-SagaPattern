package saga.camunda.travelservice.api.dto;

import java.io.Serializable;

public class BookTripResponse implements Serializable {

    private final long tripId;

    private String bookingStatus;

    private BookTripResponse() {
        tripId = -1;
    }

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
