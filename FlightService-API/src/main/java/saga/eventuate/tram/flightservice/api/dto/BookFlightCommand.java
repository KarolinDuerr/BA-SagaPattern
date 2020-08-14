package saga.eventuate.tram.flightservice.api.dto;

import io.eventuate.tram.commands.common.Command;

import java.util.Date;
import java.util.List;

public class BookFlightCommand implements Command {

    private final long tripId;

    private LocationDTO home;

    private LocationDTO destination;

    private Date outboundFlightDate;

    private Date returnFlightDate;

    private boolean oneWay;

    private List<String> travellerNames;

    private BookFlightCommand() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public BookFlightCommand(final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final boolean oneWay, final List<String> travellerNames) {
        this.tripId = -1; // no trip assigned to this booking
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.oneWay = oneWay;
        this.travellerNames = travellerNames;
    }

    public BookFlightCommand(final long tripId, final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final boolean oneWay, final List<String> travellerNames) {
        this.tripId = tripId;
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.oneWay = oneWay;
        this.travellerNames = travellerNames;
    }

    public long getTripId() {
        return tripId;
    }

    public LocationDTO getHome() {
        return home;
    }

    public void setHome(final LocationDTO home) {
        this.home = home;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(final LocationDTO destination) {
        this.destination = destination;
    }

    public Date getOutboundFlightDate() {
        return outboundFlightDate;
    }

    public void setOutboundFlightDate(final Date outboundFlightDate) {
        this.outboundFlightDate = outboundFlightDate;
    }

    public Date getReturnFlightDate() {
        return returnFlightDate;
    }

    public void setReturnFlightDate(final Date returnFlightDate) {
        this.returnFlightDate = returnFlightDate;
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

    @Override
    public String toString() {
        return "BookFlightCommand{" +
                "tripId=" + tripId +
                ", home=" + home +
                ", destination=" + destination +
                ", outboundFlightDate=" + outboundFlightDate +
                ", returnFlightDate=" + returnFlightDate +
                ", oneWay=" + oneWay +
                ", travellerNames=" + travellerNames +
                '}';
    }
}
