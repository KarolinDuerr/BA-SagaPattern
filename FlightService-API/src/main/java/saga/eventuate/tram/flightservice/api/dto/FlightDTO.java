package saga.eventuate.tram.flightservice.api.dto;

import java.util.Date;

public class FlightDTO {

    private String country;

    private String fromAirport;

    private String toAirport;

    private Date flightDateDeparture;

    private Date flightDateArrival;

    private String seatNumber;

    public FlightDTO() {

    }

    public FlightDTO(final String country, final String fromAirport, final String toAirport, final Date flightDateDeparture, final Date flightDateArrival, final String seatNumber) {
        this.country = country;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightDateDeparture = flightDateDeparture;
        this.flightDateArrival = flightDateArrival;
        this.seatNumber = seatNumber;
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

    public void setFlightDateDeparture(final Date flightDateDeparture) {
        this.flightDateDeparture = flightDateDeparture;
    }

    public Date getFlightDateArrival() {
        return flightDateArrival;
    }

    public void setFlightDateArrival(final Date flightDateArrival) {
        this.flightDateArrival = flightDateArrival;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(final String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "FlightDTO{" +
                "country='" + country + '\'' +
                ", fromAirport='" + fromAirport + '\'' +
                ", toAirport='" + toAirport + '\'' +
                ", flightDateDeparture=" + flightDateDeparture +
                ", flightDateArrival=" + flightDateArrival +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
