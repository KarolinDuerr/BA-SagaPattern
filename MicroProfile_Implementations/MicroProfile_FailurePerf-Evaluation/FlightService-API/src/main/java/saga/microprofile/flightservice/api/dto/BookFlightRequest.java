package saga.microprofile.flightservice.api.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class BookFlightRequest implements Serializable {

    private long tripId;

    private LocationDTO home;

    private LocationDTO destination;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate outboundFlightDate;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate returnFlightDate;

    private String travellerName;

    public BookFlightRequest() {
        this.tripId = -1; // no trip assigned to this booking
    }

    public BookFlightRequest(final LocationDTO home, final LocationDTO destination, final LocalDate outboundFlightDate,
                             final LocalDate returnFlightDate, final String travellerName) {
        this.tripId = -1; // no trip assigned to this booking
        this.home = home;
        this.destination = destination;
        this.outboundFlightDate = outboundFlightDate;
        this.returnFlightDate = returnFlightDate;
        this.travellerName = travellerName;
    }

    public BookFlightRequest(final long tripId, final LocationDTO home, final LocationDTO destination, final LocalDate outboundFlightDate,
                             final LocalDate returnFlightDate, final String travellerName) {
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

    public void setTripId(final long tripId) {
        this.tripId = tripId;
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
    public LocalDate getOutboundFlightDate() {
        return outboundFlightDate;
    }

    public void setOutboundFlightDate(final LocalDate outboundFlightDate) {
        this.outboundFlightDate = outboundFlightDate;
    }

    @JsonbProperty("returnFlightDate")
    public LocalDate getReturnFlightDate() {
        return returnFlightDate;
    }

    public void setReturnFlightDate(final LocalDate returnFlightDate) {
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

