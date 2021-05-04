package saga.microprofile.hotelservice.model.dto;

import saga.microprofile.hotelservice.api.dto.DestinationDTO;
import saga.microprofile.hotelservice.api.dto.StayDurationDTO;

public class HotelBookingInformationDTO {

    private DestinationDTO destination;

    private StayDurationDTO duration;

    private String boardType;

    private final long tripId;

    private HotelBookingInformationDTO() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public HotelBookingInformationDTO(final DestinationDTO destination, final StayDurationDTO duration,
                                      final String boardType) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.tripId = -1; // no trip assigned to this booking
    }

    public HotelBookingInformationDTO(final DestinationDTO destination, final StayDurationDTO duration,
                                      final String boardType, final long tripId) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.tripId = tripId;
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

    @Override
    public String toString() {
        return "HotelBookingInformationDTO{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
                ", tripId=" + tripId +
                '}';
    }
}
