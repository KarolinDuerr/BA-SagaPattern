package saga.camunda.flightservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.camunda.flightservice.error.FlightException;
import saga.camunda.flightservice.model.FindAndBookFlightInformation;
import saga.camunda.flightservice.model.FlightInformation;

import java.util.List;

@Qualifier("FlightService")
public interface IFlightService {

    List<FlightInformation> getFlightsInformation();

    FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException;

    FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException;

    void cancelFlightBooking(final Long tripId, final String travellerName);
}
