package saga.eventuate.tram.flightservice.model;

import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;

import javax.persistence.*;
import java.util.List;

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
    @JoinColumn(name="outboundFlightId", referencedColumnName = "id")
    private Flight outboundFlight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="returnFlightId", referencedColumnName = "id")
    private Flight returnFlight;

    private boolean oneWay;

    @ElementCollection
    private List<String> travellerNames;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private long tripId;

    public FlightInformation() {

    }

    public FlightInformation(final Flight outboundFlight, final Flight returnFlight, final boolean oneWay,
                             final List<String> travellerNames) throws FlightException {
        this.oneWay = oneWay;
        if (!oneWay) {
            validateFlightDates(outboundFlight, returnFlight);
        }
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerNames = travellerNames;
        this.tripId = -1; // no trip assigned to this booking
        this.bookingStatus = BookingStatus.APPROVED;
    }

    public FlightInformation(final Flight outboundFlight, final Flight returnFlight, final boolean oneWay,
                             final List<String> travellerNames, final long tripId) throws FlightException {
        this.oneWay = oneWay;
        if (!oneWay) {
            validateFlightDates(outboundFlight, returnFlight);
        }
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerNames = travellerNames;
        this.tripId = tripId;
        this.bookingStatus = BookingStatus.APPROVED;
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
        if (!oneWay) {
            validateFlightDates(outboundFlight, returnFlight);
        }
        this.outboundFlight = outboundFlight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(final Flight returnFlight) throws FlightException {
        if (!oneWay) {
            validateFlightDates(outboundFlight, returnFlight);
        }
        this.returnFlight = returnFlight;
    }

    public boolean getOneWay() {
        return oneWay;
    }

    public void setOneWay(final boolean oneWay) {
        this.oneWay = oneWay;
    }

    public List<String> getTravellerNames() {
        return travellerNames;
    }

    public void setTravellerNames(final List<String> travellerNames) {
        this.travellerNames = travellerNames;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    private void validateFlightDates(Flight outboundFlight, Flight returnFlight) throws FlightException {
        if (outboundFlight == null  || returnFlight == null) {
            return;
        }

        if (returnFlight.getFlightDate().before(outboundFlight.getFlightDate())) {
            throw new FlightException(ErrorType.INVALID_PARAMETER, "The date of the return flight is before the actual " +
                    "outbound flight.");
        }
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "FlightInformation{" +
                "id=" + id +
                ", version=" + version +
                ", outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", oneWay=" + oneWay +
                ", travellerNames=" + travellerNames +
                ", bookingStatus=" + bookingStatus +
                ", tripId=" + tripId +
                '}';
    }
}
