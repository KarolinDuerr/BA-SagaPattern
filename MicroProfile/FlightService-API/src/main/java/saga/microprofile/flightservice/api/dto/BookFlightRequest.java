package saga.microprofile.flightservice.api.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Date;

public class BookFlightRequest {

    private final long tripId;

    private LocationDTO home;

    private LocationDTO destination;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private Date outboundFlightDate;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private Date returnFlightDate;

    private String travellerName;

    public BookFlightRequest() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public BookFlightRequest(final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final String travellerName) {
        this.tripId = -1; // no trip assigned to this booking
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    public BookFlightRequest(final long tripId, final LocationDTO home, final LocationDTO destination, final Date outboundFlightDate,
                             final Date returnFlightDate, final String travellerName) {
        this.tripId = tripId;
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    @JsonbProperty("tripId")
    public long getTripId() {
        return tripId;
    }

    @JsonbProperty("home")
    public LocationDTO getHome() {
        return home;
    }

    public void setHome(final LocationDTO home) {
        this.home = home;
    }

    @JsonbProperty("destination")
    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(final LocationDTO destination) {
        this.destination = destination;
    }

    @JsonbProperty("outboundFlightDate")
    public Date getOutboundFlightDate() {
        return outboundFlightDate;
    }

    public void setOutboundFlightDate(final Date outboundFlightDate) {
        this.outboundFlightDate = outboundFlightDate;
    }

    @JsonbProperty("returnFlightDate")
    public Date getReturnFlightDate() {
        return returnFlightDate;
    }

    public void setReturnFlightDate(final Date returnFlightDate) {
        this.returnFlightDate = returnFlightDate;
    }

    @JsonbProperty("travellerName")
    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    @Override
    public String toString() {
        return "BookFlightRequest{" +
                "tripId=" + tripId +
                ", home=" + home +
                ", destination=" + destination +
                ", outboundFlightDate=" + outboundFlightDate +
                ", returnFlightDate=" + returnFlightDate +
                ", travellerName='" + travellerName + '\'' +
                '}';
    }
}

