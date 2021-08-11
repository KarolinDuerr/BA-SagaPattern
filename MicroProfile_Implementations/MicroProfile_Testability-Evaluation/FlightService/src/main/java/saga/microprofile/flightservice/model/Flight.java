package saga.microprofile.flightservice.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    private Flight() {

    }

    public Flight(final String country, final String fromAirport, final String toAirport, final Date flightDate) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDate = flightDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Flight)) {
            return false;
        }

        Flight flight = (Flight) o;

        if (Objects.equals(flight.getId(), this.getId())) {
            return true;
        }

        if (flight.getFlightDate().compareTo(this.getFlightDate()) != 0 && flight.getFlightDate().getTime() != this.getFlightDate().getTime()) {
            return false;
        }

        if (flight.getFromAirport() == null || !flight.getFromAirport().equalsIgnoreCase(this.getFromAirport())) {
            return false;
        }

        return flight.getToAirport() != null && flight.getToAirport().equalsIgnoreCase(this.getToAirport());
    }
}
