package saga.microprofile.flightservice.model.dto;


import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.Date;

public class FlightDTO  implements Serializable {

    private String country;

    private String fromAirport;

    private String toAirport;

    @JsonbDateFormat(value = "yyyy-MM-dd hh:mm:ss")
    private Date flightDate;

    public FlightDTO() {

    }

    public FlightDTO(final String country, final String fromAirport, final String toAirport, final Date flightDate) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDate = flightDate;
    }

    @JsonbProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @JsonbProperty("fromAirport")
    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(final String fromAirport) {
        this.fromAirport = fromAirport;
    }

    @JsonbProperty("toAirport")
    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(final String toAirport) {
        this.toAirport = toAirport;
    }

    @JsonbProperty("flightDate")
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
