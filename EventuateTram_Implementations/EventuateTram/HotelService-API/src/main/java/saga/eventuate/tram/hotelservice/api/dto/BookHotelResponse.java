package saga.eventuate.tram.hotelservice.api.dto;

public class BookHotelResponse {

    private long bookingId;

    private String hotelName;

    private String bookingStatus;

    private BookHotelResponse() {

    }

    public BookHotelResponse(final long bookingId, final String hotelName, final String bookingStatus) {
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingStatus = bookingStatus;
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
                "bookingId=" + bookingId +
                ", hotelName='" + hotelName + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
