package saga.eventuate.tram.flightservice.model;

import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;

import java.util.Date;

public class FindAndBookFlightInformation {

    private final long tripId;

    private Location home;

    private Location destination;

    private Date outboundFlightDate;

    private Date returnFlightDate;

    private String travellerName;

    private FindAndBookFlightInformation() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public FindAndBookFlightInformation(final Location home, final Location destination, final Date outboundFlightDate,
                                        final Date returnFlightDate, final String travellerName) throws FlightException {
        validateFlightDates(outboundFlightDate, returnFlightDate);

        this.tripId = -1; // no trip assigned to this booking
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    public FindAndBookFlightInformation(final long tripId, final Location home, final Location destination,
                                        final Date outboundFlightDate, final Date returnFlightDate,
                                        final String travellerName) throws FlightException {
        validateFlightDates(outboundFlightDate, returnFlightDate);

        this.tripId = tripId;
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    public long getTripId() {
        return tripId;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(final Location home) {
        this.home = home;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(final Location destination) {
        this.destination = destination;
    }

    public Date getOutboundFlightDate() {
        return outboundFlightDate;
    }

    public void setOutboundFlightDate(final Date outboundFlightDate) throws FlightException {
        validateFlightDates(outboundFlightDate, returnFlightDate);
        this.outboundFlightDate = outboundFlightDate;
    }

    public Date getReturnFlightDate() {
        return returnFlightDate;
    }

    public void setReturnFlightDate(final Date returnFlightDate) throws FlightException {
        validateFlightDates(outboundFlightDate, returnFlightDate);
        this.returnFlightDate = returnFlightDate;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    private void validateFlightDates(final Date outboundFlightDate, final Date returnFlightDate) throws FlightException {
        if (outboundFlightDate == null || returnFlightDate == null) {
            return;
        }

        if (returnFlightDate.before(outboundFlightDate)) {
            throw new FlightException(ErrorType.INVALID_PARAMETER, "The date of the return flight is before the " +
                    "actual outbound flight.");
        }
    }

    @Override
    public String toString() {
        return "FindAndBookFlightInformation{" +
                "tripId=" + tripId +
                ", home=" + home +
                ", destination=" + destination +
                ", outboundFlightDate=" + outboundFlightDate +
                ", returnFlightDate=" + returnFlightDate +
                ", travellerName=" + travellerName +
                '}';
    }
}
