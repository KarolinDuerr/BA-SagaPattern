package saga.eventuate.tram.flightservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.eventuate.tram.commands.common.Command;

import java.util.Date;
import java.util.List;

public class BookFlightCommand implements Command {

    private final long tripId;

    private LocationDTO home;

    private LocationDTO destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date outboundFlightDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date returnFlightDate;

    private String travellerName;

    private BookFlightCommand() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public BookFlightCommand(final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final String travellerName) {
        this.tripId = -1; // no trip assigned to this booking
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    public BookFlightCommand(final long tripId, final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final String travellerName) {
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

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    @Override
    public String toString() {
        return "BookFlightCommand{" +
                "tripId=" + tripId +
                ", home=" + home +
                ", destination=" + destination +
                ", outboundFlightDate=" + outboundFlightDate +
                ", returnFlightDate=" + returnFlightDate +
                ", travellerName=" + travellerName +
                '}';
    }
}
