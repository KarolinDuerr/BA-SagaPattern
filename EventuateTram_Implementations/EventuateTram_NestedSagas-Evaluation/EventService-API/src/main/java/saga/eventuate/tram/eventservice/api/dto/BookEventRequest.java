package saga.eventuate.tram.eventservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class BookEventRequest implements Command {

    private final long hotelBookingId;

    private String travellerName;

    private long eventId;

    private BookEventRequest() {
        this.hotelBookingId = -1; // no hotel booking assigned to this booking
    }

    public BookEventRequest(final long eventId, final String travellerName) {
        this.hotelBookingId = -1; // no hotel booking assigned to this booking
        this.eventId = eventId;
        this.travellerName = travellerName;
    }

    public BookEventRequest(final long hotelBookingId, final long eventId, final String travellerName) {
        this.hotelBookingId = hotelBookingId;
        this.eventId = eventId;
        this.travellerName = travellerName;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(String travellerName) {
        this.travellerName = travellerName;
    }

    @Override
    public String toString() {
        return "BookEventRequest{" +
                "hotelBookingId=" + hotelBookingId +
                ", travellerName='" + travellerName + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
