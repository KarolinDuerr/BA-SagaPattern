package saga.eventuate.tram.flightservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.FlightInformation;

import java.util.List;

@Qualifier("FlightService")
public interface IFlightService {

    @Transactional
    List<FlightInformation> getFlightsInformation();

    @Transactional
    FlightInformation getFlightInformation(final Long flightBookingId) throws FlightException;

    @Transactional
    FlightInformation findAndBookFlight(final FindAndBookFlightInformation findAndBookFlightInformation) throws FlightException;

    @Transactional
    void cancelFlightBooking(final Long flightBookingId, final Long tripId);
}
