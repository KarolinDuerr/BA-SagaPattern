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

    private String travellerName;

    private String boardType;

    private long customerId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private long hotelId = -1;

    private long flightId = -1;

    private TripInformation() {

    }

    public TripInformation(final TripDuration duration, final Location start, final Location destination,
                           final String travellerName, final String boardType, final long customerId) {
        this.duration = duration;
        this.start = start;
        this.destination = destination;
        this.travellerName = travellerName;
        this.boardType = boardType;
        this.customerId = customerId;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(final long hotelId) {
        this.hotelId = hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(final long flightId) {
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

    public void reject(final BookingStatus bookingStatus) {
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
                ", travellerName=" + travellerName +
                ", boardType=" + boardType +
                ", customerId=" + customerId +
                ", bookingStatus=" + bookingStatus +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TripInformation)) {
            return false;
        }

        TripInformation tripInfo = (TripInformation) o;

        if (tripInfo.getId() == this.getId()) {
            return true;
        }

        if (!tripInfo.getDuration().equals(this.getDuration())) {
            return false;
        }

        if (!tripInfo.getStart().equals(this.getStart())) {
            return false;
        }

        if (!tripInfo.getDestination().equals(this.getDestination())) {
            return false;
        }

        return tripInfo.getBoardType().equalsIgnoreCase(this.boardType)
                && tripInfo.getTravellerName().equalsIgnoreCase(this.getTravellerName());
    }
}
