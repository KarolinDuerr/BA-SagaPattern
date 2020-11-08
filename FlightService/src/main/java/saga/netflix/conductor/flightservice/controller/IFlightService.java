package saga.netflix.conductor.flightservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.model.FindAndBookFlightInformation;
import saga.netflix.conductor.flightservice.model.FlightInformation;

import java.util.List;

@Qualifier("FlightService")
public interface IFlightService {

    List<FlightInformation> getFlightsInformation();

    FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException;

    FlightInformation bookFlight(final FlightInformation flightInformation);

    FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException;
}
