package saga.netflix.conductor.travelservice.api.dto;

public class BookTripRequest {

    private TripDurationDTO duration;

    private LocationDTO start;

    private LocationDTO destination;

    private String travellerName;

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
