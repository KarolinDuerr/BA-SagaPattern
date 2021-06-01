package saga.eventuate.tram.eventservice.api.dto;

public class BookEventResponse {

    private long eventBookingId;

    private String eventName;

    private String bookingStatus;

    private BookEventResponse() {

    }

    public BookEventResponse(final String eventName, final long eventBookingId, final String bookingStatus) {
        this.eventName = eventName;
        this.eventBookingId = eventBookingId;
        this.bookingStatus = bookingStatus;
    }

    public String getEventName() {
        return eventName;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "BookEventResponse{" +
                "eventBookingId=" + eventBookingId +
                ", eventName=" + eventName +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
