package saga.netflix.conductor.flightservice.model;

import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.error.UnsupportedStateTransition;

import javax.persistence.*;

@Entity
@Table(name = "flightsInformation")
@Access(AccessType.FIELD)
public class FlightInformation {

    @Id
    @GeneratedValue
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

    private void validateFlightDates(final Flight outboundFlight, final Flight returnFlight) throws FlightException {
        if (outboundFlight == null || returnFlight == null) {
            return;
        }

        if (returnFlight.getFlightDate().before(outboundFlight.getFlightDate())) {
            throw new FlightException(ErrorType.INVALID_PARAMETER, "The date of the return flight is before the " +
                    "actual " +
                    "outbound flight.");
        }
    }

    public void cancel(final BookingStatus bookingStatus) {
        switch (this.bookingStatus) {
            case PENDING:
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
                this.bookingStatus = BookingStatus.CONFIRMED;
                break;
            default:
                throw new UnsupportedStateTransition("The flight booking  can only be confirmed if its still PENDING," +
                        " " +
                        "but the current status is: " + getBookingStatus());
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

        if (flightInformation.getId() == this.getId()) {
            return true;
        }

        if (!flightInformation.getTravellerName().equals(this.getTravellerName())) {
            return false;
        }

        if (flightInformation.getOutboundFlight().equals(this.getOutboundFlight()) && flightInformation.getReturnFlight().equals(this.getReturnFlight())) {
            return true;
        }
        return false;
    }
}
