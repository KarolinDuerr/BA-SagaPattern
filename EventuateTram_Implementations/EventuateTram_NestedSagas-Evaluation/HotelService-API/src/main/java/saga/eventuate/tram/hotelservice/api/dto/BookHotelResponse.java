package saga.eventuate.tram.hotelservice.api.dto;

public class BookHotelResponse {

    private long bookingId;

    private String hotelName;

    private final long eventBookingId;

    private String bookingStatus;

    private BookHotelResponse() {
        this.eventBookingId = -1; // no event booked for this hotel booking
    }

    public BookHotelResponse(final long bookingId, final String hotelName, final String bookingStatus) {
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingStatus = bookingStatus;
        this.eventBookingId = -1; // no event booked for this hotel booking
    }

    public BookHotelResponse(final long bookingId, final String hotelName, final String bookingStatus,
                             final long eventBookingId) {
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingStatus = bookingStatus;
        this.eventBookingId = eventBookingId;
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

    public long getEventBookingId() {
        return eventBookingId;
    }

    @Override
    public String toString() {
        return "BookHotelResponse{" +
                "bookingId=" + bookingId +
                ", hotelName='" + hotelName + '\'' +
                ", eventBookingId=" + eventBookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
