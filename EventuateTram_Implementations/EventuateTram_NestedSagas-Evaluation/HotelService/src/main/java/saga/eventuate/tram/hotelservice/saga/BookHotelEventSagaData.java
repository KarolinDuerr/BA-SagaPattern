package saga.eventuate.tram.hotelservice.saga;

import saga.eventuate.tram.eventservice.api.dto.BookEventRequest;
import saga.eventuate.tram.hotelservice.model.RejectionReason;

public class BookHotelEventSagaData {

    private final long hotelBookingId;

    private String travellerName;

    private long eventId;

    private long eventBookingId;

    private RejectionReason rejectionReason;

    private BookHotelEventSagaData() {
        this.hotelBookingId = 0;
        this.eventId = -1;
        this.eventBookingId = -1;
    }

    public BookHotelEventSagaData(final long hotelBookingId, final String travellerName, final long eventId) {
        this.hotelBookingId = hotelBookingId;
        this.travellerName = travellerName;
        this.eventId = eventId;
        this.eventBookingId = -1;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(String travellerName) {
        this.travellerName = travellerName;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    public void setEventBookingId(long eventBookingId) {
        this.eventBookingId = eventBookingId;
    }

    public BookEventRequest makeBookEventRequest() {
        return new BookEventRequest(getHotelBookingId(), getEventId(), getTravellerName());
    }

//    public BookFlightCommand makeBookFlightCommand() {
//        LocationDTO home = new LocationDTO(tripInformation.getStart().getCountry(),
//                tripInformation.getStart().getCity());
//        LocationDTO destination = new LocationDTO(tripInformation.getDestination().getCountry(),
//                tripInformation.getDestination().getCity());
//        return new BookFlightCommand(getHotelId(), home, destination, tripInformation.getDuration().getStart(),
//                tripInformation.getDuration().getEnd(), tripInformation.getTravellerName());
//    }


    @Override
    public String toString() {
        return "BookHotelEventSagaData{" +
                "hotelBookingId=" + hotelBookingId +
                ", travellerName='" + travellerName + '\'' +
                ", eventId=" + eventId +
                ", eventBookingId=" + eventBookingId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
