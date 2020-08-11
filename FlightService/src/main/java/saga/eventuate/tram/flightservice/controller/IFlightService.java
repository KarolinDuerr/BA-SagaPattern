package saga.eventuate.tram.flightservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FlightInformation;

import java.util.List;

@Qualifier("FlightService")
public interface IFlightService {

    List<FlightInformation> getFlightsInformation();

    FlightInformation getFlightInformation(Long flightBookingId) throws FlightException;

    FlightInformation bookFlight(FlightInformation flightInformation);

    FlightInformation bookFlight(FlightInformation flightInformation, int tripId);

    boolean cancelFlightBooking(Long flightBookingId) throws FlightException;

}
