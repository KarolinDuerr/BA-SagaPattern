package saga.eventuate.tram.travelservice.model.dto;

import saga.eventuate.tram.travelservice.api.dto.LocationDTO;
import saga.eventuate.tram.travelservice.api.dto.TripDurationDTO;
import saga.eventuate.tram.travelservice.model.BookingStatus;

import java.util.List;

public class TripInformationDTO {

    private Long id;

    private TripDurationDTO duration;

    private LocationDTO start;

    private LocationDTO destination;

    private String travellerName;

    private String boardType;

    private long eventId = 0;

    private long customerId;

    private ConfirmationInformationDTO confirmationInformation;

    private TripInformationDTO() {

    }

    public TripInformationDTO(final long id, final TripDurationDTO duration, final LocationDTO start,
                              final LocationDTO destination, final String travellerName,
                              final String boardType, final long customerId, final long eventId,
                              final ConfirmationInformationDTO confirmationInformation) {
        this.id = id;
        this.duration = duration;
        this.destination = destination;
        this.start = start;
        this.travellerName = travellerName;
        this.boardType = boardType;
        this.eventId = eventId;
        this.customerId = customerId;
        this.confirmationInformation = confirmationInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TripDurationDTO getDuration() {
        return duration;
    }

    public void setDuration(final TripDurationDTO duration) {
        this.duration = duration;
    }

    public LocationDTO getStart() {
        return start;
    }

    public void setStart(final LocationDTO start) {
        this.start = start;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(final LocationDTO destination) {
        this.destination = destination;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(final String boardType) {
        this.boardType = boardType;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final long customerId) {
        this.customerId = customerId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public ConfirmationInformationDTO getConfirmationInformation() {
        return confirmationInformation;
    }

    public void setConfirmationInformation(ConfirmationInformationDTO confirmationInformation) {
        this.confirmationInformation = confirmationInformation;
    }

    @Override
    public String toString() {
        return "TripInformationDTO{" +
                "id=" + id +
                ", duration=" + duration +
                ", start=" + start +
                ", destination=" + destination +
                ", travellerName='" + travellerName + '\'' +
                ", boardType='" + boardType + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
