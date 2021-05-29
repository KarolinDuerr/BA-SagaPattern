package saga.microprofile.flightservice.controller;

import saga.microprofile.flightservice.error.FlightException;
import saga.microprofile.flightservice.model.FindAndBookFlightInformation;
import saga.microprofile.flightservice.model.FlightInformation;

import java.util.List;

public interface IFlightService {

    List<FlightInformation> getFlightsInformation();

    FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException;

    FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException;

    void cancelFlightBooking(final Long tripId, final String travellerName);
}
