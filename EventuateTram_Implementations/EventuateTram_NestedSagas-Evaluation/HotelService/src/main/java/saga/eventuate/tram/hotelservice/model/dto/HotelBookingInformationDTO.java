package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;

public class HotelBookingInformationDTO {

    private DestinationDTO destination;

    private StayDurationDTO duration;

    private String boardType;

    private final long tripId;

    private final long eventId;

    private HotelBookingInformationDTO() {
        this.tripId = -1; // no trip assigned to this booking
        this.eventId = 0; // no event booked for this booking
    }

    public HotelBookingInformationDTO(final DestinationDTO destination, final StayDurationDTO duration,
                                      final String boardType) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.tripId = -1; // no trip assigned to this booking
        this.eventId = 0; // no event booked for this booking
    }

    public HotelBookingInformationDTO(final DestinationDTO destination, final StayDurationDTO duration,
                                      final String boardType, final long tripId, final long eventId) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.tripId = tripId;
        this.eventId = eventId;
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

    public long getTripId() {
        return tripId;
    }

    public long getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "HotelBookingInformationDTO{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
                ", tripId=" + tripId +
                ", eventId=" + eventId +
                '}';
    }
}
