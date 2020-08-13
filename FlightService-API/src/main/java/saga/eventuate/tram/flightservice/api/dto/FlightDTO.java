package saga.eventuate.tram.flightservice.api.dto;

import java.util.Date;

public class FlightDTO {

    private String country;

    private String fromAirport;

    private String toAirport;

    private Date flightDate;

    public FlightDTO() {

    }

    public FlightDTO(final String country, final String fromAirport, final String toAirport, final Date flightDate) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDate = flightDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(final String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(final String toAirport) {
        this.toAirport = toAirport;
    }

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
