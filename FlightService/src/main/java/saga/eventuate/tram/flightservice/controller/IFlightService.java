package saga.eventuate.tram.flightservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformation;

import java.util.List;

@Qualifier("FlightService")
public interface IFlightService {

    List<FlightInformation> getFlightsInformation();

    FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException;

    FlightInformation bookFlight(final FlightInformation flightInformation);

    FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException;

    boolean cancelFlightBooking(final Long flightBookingId) throws FlightException;

}
