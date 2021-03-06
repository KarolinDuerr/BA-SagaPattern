package saga.microprofile.travelservice.model;

import saga.microprofile.travelservice.error.UnsupportedStateTransition;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

@Entity
@Table(name = "tripsInformation")
@Access(AccessType.FIELD)
@NamedQuery(name = "TripInformation.findAll", query = "SELECT trips FROM TripInformation trips")
@NamedQuery(name = "TripInformation.findTripByName", query = "SELECT trips FROM TripInformation trips WHERE " +
        "trips.travellerName = :travellerName")
@NamedQuery(name = "TripInformation.findByLraId", query = "SELECT trips FROM TripInformation trips WHERE " +
        "trips.lraId = :lraId")
public class TripInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    private String lraId;

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

    public TripInformation() {

    }

    public TripInformation(final URI lraId, final TripDuration duration, final Location start,
                           final Location destination, final String travellerName, final String boardType,
                           final long customerId) {
        this.lraId = String.valueOf(lraId);
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getLraId() {
        return lraId;
    }

    public void setLraId(final URI lraId) {
        this.lraId = lraId.toString();
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
            case CANCELLED:
                this.bookingStatus = BookingStatus.CANCELLED;
                break;
            default:
                throw new UnsupportedStateTransition("The trip can only be rejected if its still PENDING or " +
                        "CONFIRMED, but the " +
                        "current status is: " + getBookingStatus());
        }
    }

    public void reject() {
        switch (this.bookingStatus) {
            case PENDING:
            case REJECTED:
                this.bookingStatus = BookingStatus.REJECTED;
                break;
            default:
                throw new UnsupportedStateTransition("The trip can only be rejected if its still PENDING, but the " +
                        "current status is: " + getBookingStatus());
        }
    }

    public void confirm() {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
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
                ", lraId='" + lraId + '\'' +
                ", duration=" + duration +
                ", start=" + start +
                ", destination=" + destination +
                ", travellerName='" + travellerName + '\'' +
                ", boardType='" + boardType + '\'' +
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

        if (Objects.equals(tripInfo.getId(), this.getId())) {
            return true;
        }

        if (!Objects.equals(tripInfo.getDuration(), this.getDuration())) {
            return false;
        }

        if (!Objects.equals(tripInfo.getStart(), this.getStart())) {
            return false;
        }

        if (!Objects.equals(tripInfo.getDestination(), this.getDestination())) {
            return false;
        }

        if (tripInfo.getBoardType() == null || !tripInfo.getBoardType().equalsIgnoreCase(this.boardType)) {
            return false;
        }

        return tripInfo.getTravellerName() != null && tripInfo.getTravellerName().equalsIgnoreCase(this.getTravellerName());
    }
}
