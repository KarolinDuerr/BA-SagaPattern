package saga.eventuate.tram.hotelservice.api.dto;

public class CancelHotelResponse {

    private long bookingId;

    private String hotelName;

    private String bookingStatus;

    private CancelHotelResponse() {

    }

    public CancelHotelResponse(final long bookingId, final String hotelName, final String bookingStatus) {
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
        return "CancelHotelResponse{" +
                "bookingId=" + bookingId +
                ", hotelName='" + hotelName + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
