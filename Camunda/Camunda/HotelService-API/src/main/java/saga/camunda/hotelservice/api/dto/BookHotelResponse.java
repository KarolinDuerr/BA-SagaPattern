package saga.camunda.hotelservice.api.dto;

import java.io.Serializable;

public class BookHotelResponse implements Serializable {

    private long tripId;

    private long bookingId;

    private String hotelName;

    private String bookingStatus;

    private BookHotelResponse() {

    }

    public BookHotelResponse(final long bookingId, final String hotelName, final String bookingStatus) {
        this.tripId = -1;
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingStatus = bookingStatus;
    }

    public BookHotelResponse(final long tripId, final long bookingId, final String hotelName, final String bookingStatus) {
        this.tripId = tripId;
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingStatus = bookingStatus;
    }

    public long getTripId() {
        return tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "BookHotelResponse{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                ", hotelName='" + hotelName + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
