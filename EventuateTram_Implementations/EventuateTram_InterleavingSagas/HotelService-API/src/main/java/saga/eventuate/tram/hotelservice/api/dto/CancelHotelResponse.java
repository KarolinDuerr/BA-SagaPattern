package saga.eventuate.tram.hotelservice.api.dto;

public class CancelHotelResponse {

    private long bookingId;

    private String bookingStatus;

    private CancelHotelResponse() {

    }

    public CancelHotelResponse(final long bookingId, final String bookingStatus) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
    }

    public long getBookingId() {
        return bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "CancelHotelResponse{" +
                "bookingId=" + bookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}