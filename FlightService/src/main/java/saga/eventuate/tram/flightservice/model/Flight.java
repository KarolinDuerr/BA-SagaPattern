package saga.eventuate.tram.flightservice.model;

import saga.eventuate.tram.flightservice.error.FlightException;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "flights")
@Access(AccessType.FIELD)
public class Flight {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private String country;

    private String fromAirport;

    private String toAirport;

    private Date flightDate;

    public Flight() {

    }

    public Flight(final String country, final String fromAirport, final String toAirport,
                  final Date flightDate) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDate = flightDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Flight{" +
                "id=" + id +
                ", version=" + version +
                ", country='" + country + '\'' +
                ", fromAirport='" + fromAirport + '\'' +
                ", toAirport='" + toAirport + '\'' +
                ", flightDate=" + flightDate +
                '}';
    }
}