package saga.microprofile.hotelservice.api.dto;

public class BookHotelResponse {

    private long tripId;

    private long bookingId;

    private String hotelName;

    private String bookingStatus;

    // default constructor necessary for unmarshalling
    public BookHotelResponse() {

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

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(final long bookingId) {
        this.bookingId = bookingId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(final String hotelName) {
        this.hotelName = hotelName;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final String bookingStatus) {
        this.bookingStatus = bookingStatus;
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
