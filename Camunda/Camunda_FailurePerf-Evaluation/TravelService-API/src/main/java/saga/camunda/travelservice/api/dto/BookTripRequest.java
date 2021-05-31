package saga.camunda.travelservice.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BookTripRequest implements Serializable {

    @NotNull(message = "Trip duration cannot be missing")
    private TripDurationDTO duration;

    @NotNull(message = "The trip's start location cannot be missing")
    private LocationDTO start;

    @NotNull(message = "The trip's destination cannot be missing")
    private LocationDTO destination;

    @NotBlank(message = "Start date cannot be missing")
    private String travellerName;

    @NotBlank(message = "Board type cannot be missing")
    private String boardType;

    private long customerId;

    private BookTripRequest() {

    }

    private BookTripRequest(final TripDurationDTO duration, final LocationDTO start, final LocationDTO destination,
                            final String travellerName, final String boardType, final long customerId) {
        this.duration = duration;
        this.destination = destination;
        this.start = start;
        this.travellerName = travellerName;
        this.boardType = boardType;
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "BookTripRequest{" +
                "duration=" + duration +
                ", start=" + start +
                ", destination=" + destination +
                ", travellerName='" + travellerName + '\'' +
                ", boardType='" + boardType + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
