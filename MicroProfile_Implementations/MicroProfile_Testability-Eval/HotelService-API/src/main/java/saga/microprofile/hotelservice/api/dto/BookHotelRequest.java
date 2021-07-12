package saga.microprofile.hotelservice.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

//@JsonbPropertyOrder({"trip"})
public class BookHotelRequest {

    private long tripId;

    private DestinationDTO destination;

    private StayDurationDTO duration;

    private String boardType;

    private String travellerName;

    // default constructor necessary for unmarshalling
    public BookHotelRequest() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public BookHotelRequest(final DestinationDTO destination, final StayDurationDTO duration, final String boardType,
                            final String travellerName) {
        this.tripId = -1; // no trip assigned to this booking
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.travellerName = travellerName;
    }

    public BookHotelRequest(final long tripId, final DestinationDTO destination, final StayDurationDTO duration,
                            final String boardType, final String travellerName) {
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.travellerName = travellerName;
        this.tripId = tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setDestination(final DestinationDTO destination) {
        this.destination = destination;
    }

    @JsonbProperty("destination")
    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDuration(final StayDurationDTO duration) {
        this.duration = duration;
    }

    @JsonbProperty("duration")
    public StayDurationDTO getDuration() {
        return duration;
    }

    public void setBoardType(final String boardType) {
        this.boardType = boardType;
    }

    @JsonbProperty("boardType")
    public String getBoardType() {
        return boardType;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    @JsonbProperty("travellerName")
    public String getTravellerName() {
        return travellerName;
    }

    @Override
    public String toString() {
        return "BookHotelRequest{" +
                "tripId=" + tripId +
                ", destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
                ", travellerName='" + travellerName + '\'' +
                '}';
    }
}

