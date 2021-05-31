package saga.eventuate.tram.flightservice.model;

import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.error.UnsupportedStateTransition;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "flightsInformation")
@Access(AccessType.FIELD)
public class FlightInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "outboundFlightId", referencedColumnName = "id")
    private Flight outboundFlight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "returnFlightId", referencedColumnName = "id")
    private Flight returnFlight;

    private String travellerName;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private long tripId;

    private FlightInformation() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public FlightInformation(final Flight outboundFlight, final Flight returnFlight, final String travellerName) throws FlightException {
        validateFlightDates(outboundFlight, returnFlight);

        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerName = travellerName;
        this.tripId = -1; // no trip assigned to this booking
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public FlightInformation(final Flight outboundFlight, final Flight returnFlight, final String travellerName,
                             final long tripId) throws FlightException {
        validateFlightDates(outboundFlight, returnFlight);

        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerName = travellerName;
        this.tripId = tripId;
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Flight getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(final Flight outboundFlight) throws FlightException {
        validateFlightDates(outboundFlight, returnFlight);
        this.outboundFlight = outboundFlight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(final Flight returnFlight) throws FlightException {
        validateFlightDates(outboundFlight, returnFlight);
        this.returnFlight = returnFlight;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    private void validateFlightDates(Flight outboundFlight, Flight returnFlight) throws FlightException {
        if (outboundFlight == null || returnFlight == null) {
            return;
        }

        if (returnFlight.getFlightDate().before(outboundFlight.getFlightDate())) {
            throw new FlightException(ErrorType.INVALID_PARAMETER, "The date of the return flight is before the " +
                    "actual outbound flight.");
        }
    }

    public void cancel(final BookingStatus bookingStatus) {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
            case CANCELLED:
                this.bookingStatus = bookingStatus;
                break;
            default:
                throw new UnsupportedStateTransition("The flight booking can only be rejected if its still PENDING, " +
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
                throw new UnsupportedStateTransition("The flight booking  can only be confirmed if its still PENDING," +
                        " but the current status is: " + getBookingStatus());
        }
    }

    @Override
    public String toString() {
        return "FlightInformation{" +
                "id=" + id +
                ", version=" + version +
                ", outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", travellerName=" + travellerName +
                ", bookingStatus=" + bookingStatus +
                ", tripId=" + tripId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof FlightInformation)) {
            return false;
        }

        FlightInformation flightInformation = (FlightInformation) o;

        if (Objects.equals(flightInformation.getId(), this.getId())) {
            return true;
        }

        if (!Objects.equals(flightInformation.getTravellerName(), this.getTravellerName())) {
            return false;
        }

        if (!Objects.equals(flightInformation.getOutboundFlight(), this.getOutboundFlight()) || !Objects.equals(flightInformation.getReturnFlight(), this.getReturnFlight())) {
            return false;
        }

        return Objects.equals(flightInformation.getTripId(), this.getTripId());
    }
}
