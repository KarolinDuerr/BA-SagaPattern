package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class BookHotelRequest implements Command {

    private final long tripId;

    private DestinationDTO destination;

    private StayDurationDTO duration;

    private String boardType;

    private String travellerName;

    private final long eventId;

    private BookHotelRequest() {
        this.tripId = -1; // no trip assigned to this booking
        this.eventId = 0; // no event booked for this hotel booking
    }

    public BookHotelRequest(final DestinationDTO destination, final StayDurationDTO duration, final String boardType,
                            final String travellerName) {
        this.tripId = -1; // no trip assigned to this booking
        this.eventId = 0; // no event booked for this hotel booking
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.travellerName = travellerName;
    }

//    public BookHotelRequest(final long tripId, final DestinationDTO destination, final StayDurationDTO duration,
//                            final String boardType, final String travellerName) {
//        this.eventId = 0; // no event booked for this hotel booking
//        this.destination = destination;
//        this.duration = duration;
//        this.boardType = boardType;
//        this.travellerName = travellerName;
//        this.tripId = tripId;
//    }

    public BookHotelRequest(final long tripId, final DestinationDTO destination, final StayDurationDTO duration,
                            final String boardType, final String travellerName, final long eventId) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.travellerName = travellerName;
        this.tripId = tripId;
        this.eventId = eventId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setDestination(final DestinationDTO destination) {
        this.destination = destination;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDuration(final StayDurationDTO duration) {
        this.duration = duration;
    }

    public StayDurationDTO getDuration() {
        return duration;
    }

    public void setBoardType(final String boardType) {
        this.boardType = boardType;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public long getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "BookHotelRequest{" +
                "tripId=" + tripId +
                ", destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
                ", travellerName='" + travellerName + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
