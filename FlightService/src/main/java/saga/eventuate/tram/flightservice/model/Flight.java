package saga.eventuate.tram.flightservice.model;

import saga.eventuate.tram.flightservice.error.ErrorType;
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

    private Date flightDateDeparture;

    private Date flightDateArrival;

    private String seatNumber;

    public Flight() {

    }

    public Flight(final String country, final String fromAirport, final String toAirport,
                  final Date flightDateDeparture, final Date flightDateArrival, final String seatNumber) throws FlightException {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        validateFlightDates(flightDateDeparture, flightDateArrival);
        this.flightDateDeparture = flightDateDeparture;
        this.flightDateArrival = flightDateArrival;
        this.seatNumber = seatNumber;
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

    public Date getFlightDateDeparture() {
        return flightDateDeparture;
    }

    public void setFlightDateDeparture(final Date flightDateDeparture) throws FlightException {
        validateFlightDates(flightDateDeparture, this.flightDateArrival);
        this.flightDateDeparture = flightDateDeparture;
    }

    public Date getFlightDateArrival() {
        return flightDateArrival;
    }

    public void setFlightDateArrival(final Date flightDateArrival) throws FlightException {
        validateFlightDates(this.flightDateDeparture, flightDateArrival);
        this.flightDateArrival = flightDateArrival;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(final String seatNumber) {
        this.seatNumber = seatNumber;
    }

    private void validateFlightDates(Date departure, Date arrival) throws FlightException {
        if (!departure.before(arrival)) {
            throw new FlightException(ErrorType.INVALID_PARAMETER, "The date of the arrival is before the actual " +
                    "departure.");
        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
//                ", version=" + version +
                ", country='" + country + '\'' +
                ", fromAirport='" + fromAirport + '\'' +
                ", toAirport='" + toAirport + '\'' +
                ", flightDateDeparture=" + flightDateDeparture +
                ", flightDateArrival=" + flightDateArrival +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
