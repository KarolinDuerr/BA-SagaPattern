package saga.microprofile.flightservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class FlightDTO {

    private String country;

    private String fromAirport;

    private String toAirport;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date flightDate;

    private FlightDTO() {

    }

    public FlightDTO(final String country, final String fromAirport, final String toAirport, final Date flightDate) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDate = flightDate;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @JsonProperty("fromAirport")
    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(final String fromAirport) {
        this.fromAirport = fromAirport;
    }

    @JsonProperty("toAirport")
    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(final String toAirport) {
        this.toAirport = toAirport;
    }

    @JsonProperty("flightDate")
    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(final Date flightDate) {
        this.flightDate = flightDate;
    }

    @Override
    public String toString() {
        return "FlightDTO{" +
                "country='" + country + '\'' +
                ", fromAirport='" + fromAirport + '\'' +
                ", toAirport='" + toAirport + '\'' +
                ", flightDate=" + flightDate +
                '}';
    }
}
