package saga.eventuate.tram.eventservice.model.dto;

import saga.eventuate.tram.eventservice.model.BookingStatus;

public class EventBookingDTO {

    private Long id;

    private EventDTO bookedEvent;

    private String travellerName;

    private final long hotelBookingId;

    private BookingStatus bookingStatus;

    private EventBookingDTO() {
        this.hotelBookingId = -1; // no hotel booking assigned to this booking
    }

    public EventBookingDTO(final EventDTO bookedEvent, final String travellerName, BookingStatus bookingStatus) {
        this.bookedEvent = bookedEvent;
        this.travellerName = travellerName;
        this.bookingStatus = bookingStatus;
        this.hotelBookingId = -1; // no hotel booking assigned to this booking
    }

    public EventBookingDTO(final EventDTO bookedEvent, final String travellerName, BookingStatus bookingStatus,
                           final long hotelBookingId) {
        this.bookedEvent = bookedEvent;
        this.travellerName = travellerName;
        this.bookingStatus = bookingStatus;
        this.hotelBookingId = hotelBookingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventDTO getBookedEvent() {
        return bookedEvent;
    }

    public void setBookedEvent(EventDTO bookedEvent) {
        this.bookedEvent = bookedEvent;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(String travellerName) {
        this.travellerName = travellerName;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "EventBookingDTO{" +
                "id=" + id +
                ", bookedEvent=" + bookedEvent +
                ", travellerName='" + travellerName + '\'' +
                ", hotelBookingId=" + hotelBookingId +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
