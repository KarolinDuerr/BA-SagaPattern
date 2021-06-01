package saga.eventuate.tram.eventservice.model;

import saga.eventuate.tram.eventservice.error.UnsupportedStateTransition;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "eventBookings")
@Access(AccessType.FIELD)
public class EventBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private final long hotelBookingId;

    private String travellerName;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookedEventId", referencedColumnName = "id")
    private Event bookedEvent;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private EventBooking() {
        hotelBookingId = -1; // no hotel booking assigned to this booking
    }

    public EventBooking(final String travellerName, final Event bookedEvent) {
        hotelBookingId = -1; // no hotel booking assigned to this booking
        this.travellerName = travellerName;
        this.bookedEvent = bookedEvent;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public EventBooking(final long hotelBookingId, final String travellerName, final Event bookedEvent) {
        this.hotelBookingId = hotelBookingId;
        this.travellerName = travellerName;
        this.bookedEvent = bookedEvent;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public Event getBookedEvent() {
        return bookedEvent;
    }

    public void setBookedEvent(final Event newBookedEvent) {
        this.bookedEvent = newBookedEvent;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void cancel(final BookingStatus bookingStatus) {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
            case CANCELLED:
                this.bookingStatus = bookingStatus;
                break;
            default:
                throw new UnsupportedStateTransition("The event booking can only be rejected if its still PENDING, " +
                        "but the current status is: " + getBookingStatus());
        }
    }

    public void confirm() {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
                this.bookingStatus = BookingStatus.CONFIRMED;
                break;
            default:
                throw new UnsupportedStateTransition("The event booking  can only be confirmed if its still PENDING, " +
                        "but the current status is: " + getBookingStatus());
        }
    }

    @Override
    public String toString() {
        return "EventBooking{" +
                "id=" + id +
                ", version=" + version +
                ", hotelBookingId=" + hotelBookingId +
                ", travellerName='" + travellerName + '\'' +
                ", bookedEvent=" + bookedEvent +
                ", bookingStatus=" + bookingStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EventBooking)) {
            return false;
        }

        EventBooking eventBooking = (EventBooking) o;

        if (Objects.equals(eventBooking.getId(), this.getId())) {
            return true;
        }

        if (!Objects.equals(eventBooking.getTravellerName(), this.getTravellerName())) {
            return false;
        }

        if (!Objects.equals(eventBooking.getBookedEvent(), this.getBookedEvent())) {
            return false;
        }

        return Objects.equals(eventBooking.getHotelBookingId(), this.getHotelBookingId());
    }
}
