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

    private List<String> travellerNames;

    private int numberOfPersons;

    private int numberOfRooms;

    private boolean oneWayFlight;

    private long customerId;

    private BookingStatus bookingStatus;

    private long hotelId = -1;

    private long flightId = -1;

    public TripInformationDTO() {

    }

    public TripInformationDTO(final long id, final TripDurationDTO duration, final LocationDTO start,
                              final LocationDTO destination, final List<String> travellerNames,
                              final int numberOfPersons, final int numberOfRooms, final boolean oneWayFlight,
                              final long customerId, final BookingStatus bookingStatus) {
        this.id = id;
        this.duration = duration;
        this.destination = destination;
        this.start = start;
        this.travellerNames = travellerNames;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
        this.oneWayFlight = oneWayFlight;
        this.customerId = customerId;
        this.bookingStatus = bookingStatus;
    }

    public TripInformationDTO(final long id, final TripDurationDTO duration, final LocationDTO start,
                              final LocationDTO destination, final List<String> travellerNames,
                              final int numberOfPersons, final int numberOfRooms, final boolean oneWayFlight,
                              final long customerId, final BookingStatus bookingStatus, final long hotelId,
                              final long flightId) {
        this.id = id;
        this.duration = duration;
        this.destination = destination;
        this.start = start;
        this.travellerNames = travellerNames;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
        this.oneWayFlight = oneWayFlight;
        this.customerId = customerId;
        this.bookingStatus = bookingStatus;
        this.hotelId = hotelId;
        this.flightId = flightId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isOneWayFlight() {
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    @Override
    public String toString() {
        return "TripInformationDTO{" +
                "id=" + id +
                ", duration=" + duration +
                ", start=" + start +
                ", destination=" + destination +
                ", travellerNames=" + travellerNames +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                ", oneWayFlight=" + oneWayFlight +
                ", customerId=" + customerId +
                ", bookingStatus=" + bookingStatus +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                '}';
    }
}
