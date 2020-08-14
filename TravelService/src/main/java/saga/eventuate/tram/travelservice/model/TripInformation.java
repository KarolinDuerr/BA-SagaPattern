package saga.eventuate.tram.travelservice.model;

import saga.eventuate.tram.travelservice.error.UnsupportedStateTransition;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tripsInformation")
@Access(AccessType.FIELD)
public class TripInformation {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Embedded
    private TripDuration duration;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "start_country")),
            @AttributeOverride(name = "city", column = @Column(name = "start_city"))
    })
    private Location start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "destination_country")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city"))
    })
    private Location destination;

    @ElementCollection
    private List<String> travellerNames;

    private int numberOfPersons;

    private int numberOfRooms;

    private boolean oneWayFlight;

    private long customerId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private long hotelId = -1;

    private long flightId = -1;

    private TripInformation() {

    }

    public TripInformation(final TripDuration duration, final Location start, final Location destination,
                           final List<String> travellerNames, final int numberOfPersons, final int numberOfRooms,
                           final boolean oneWayFlight, final long customerId) {
        this.duration = duration;
        this.start = start;
        this.destination = destination;
        this.travellerNames = travellerNames;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
        this.oneWayFlight = oneWayFlight;
        this.customerId = customerId;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TripDuration getDuration() {
        return duration;
    }

    public void setDuration(final TripDuration duration) {
        this.duration = duration;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(final Location destination) {
        this.destination = destination;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(final Location start) {
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
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

    public void cancel() {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
                this.bookingStatus = BookingStatus.CANCELLED;
                break;
            default:
                throw new UnsupportedStateTransition("The trip can only be rejected if its still PENDING, but the " +
                        "current status is: " + getBookingStatus());
        }
    }

    public void reject(BookingStatus bookingStatus) {
        switch (this.bookingStatus) {
            case PENDING:
                this.bookingStatus = bookingStatus;
                break;
            default:
                throw new UnsupportedStateTransition("The trip can only be rejected if its still PENDING, but the " +
                        "current status is: " + getBookingStatus());
        }
    }

    public void confirm() {
        switch (this.bookingStatus) {
            case PENDING:
                this.bookingStatus = BookingStatus.CONFIRMED;
                break;
            default:
                throw new UnsupportedStateTransition("The trip can only be confirmed if its still PENDING, but the " +
                        "current status is: " + getBookingStatus());
        }
    }

    @Override
    public String toString() {
        return "TripInformation{" +
                "id=" + id +
                ", version=" + version +
                ", duration=" + duration +
                ", destination=" + destination +
                ", start=" + start +
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
