package saga.eventuate.tram.travelservice.api.dto;

import java.util.List;

public class BookTripRequest {

    private TripDurationDTO duration;

    private LocationDTO destination;

    private LocationDTO start;

    private List<String> travellerNames;

    private int numberOfPersons;

    private int numberOfRooms;

    private boolean oneWayFlight;

    private long customerId;

    private BookTripRequest() {

    }

    private BookTripRequest(final TripDurationDTO duration, final LocationDTO destination, final LocationDTO start,
                            final List<String> travellerNames, final int numberOfPersons, final int numberOfRooms,
                            final boolean oneWayFlight, final long customerId) {
        this.duration = duration;
        this.destination = destination;
        this.start = start;
        this.travellerNames = travellerNames;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
        this.oneWayFlight = oneWayFlight;
        this.customerId = customerId;
    }

    public TripDurationDTO getDuration() {
        return duration;
    }

    public void setDuration(final TripDurationDTO duration) {
        this.duration = duration;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(final LocationDTO destination) {
        this.destination = destination;
    }

    public LocationDTO getStart() {
        return start;
    }

    public void setStart(final LocationDTO start) {
        this.start = start;
    }

    public List<String> getTravellerNames() {
        return travellerNames;
    }

    public void setTravellerNames(List<String> travellerNames) {
        this.travellerNames = travellerNames;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(final int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(final int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public boolean getOneWayFlight() {
        return oneWayFlight;
    }

    public void setOneWayFlight(final boolean oneWayFlight) {
        this.oneWayFlight = oneWayFlight;
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
                ", destination=" + destination +
                ", start=" + start +
                ", travellerNames=" + travellerNames +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                ", oneWayFlight=" + oneWayFlight +
                ", customerId=" + customerId +
                '}';
    }
}
