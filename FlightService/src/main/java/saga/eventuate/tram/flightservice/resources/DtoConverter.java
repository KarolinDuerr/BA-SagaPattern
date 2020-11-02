package saga.eventuate.tram.flightservice.resources;

import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.BookFlightRequest;
import saga.eventuate.tram.flightservice.api.dto.FlightDTO;
import saga.eventuate.tram.flightservice.api.dto.LocationDTO;
import saga.eventuate.tram.flightservice.error.ConverterException;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FindAndBookFlightInformation;
import saga.eventuate.tram.flightservice.model.Flight;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.model.Location;
import saga.eventuate.tram.flightservice.model.dto.FlightInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public FlightInformation convertToFlightInformation(final BookFlightRequest bookFlightRequest) throws ConverterException, FlightException {
        if (bookFlightRequest == null) {
            throw new ConverterException("The information to book the flight is missing.");
        }

        checkIfOneWayTicketOrInformationIsMissing(bookFlightRequest);
        Flight outboundFlight = convertToFlight(bookFlightRequest.getOutboundFlight());
        Flight returnFlight = convertToFlight(bookFlightRequest.getReturnFlight());
        return new FlightInformation(outboundFlight, returnFlight, bookFlightRequest.getTravellerName());
    }

    public FindAndBookFlightInformation convertToFindAndBookFlightInformation(final BookFlightCommand bookFlightCommand) throws ConverterException, FlightException {
        if (bookFlightCommand == null) {
            throw new ConverterException("The information to find and book a flight is missing.");
        }

        checkIfOneWayTicketOrInformationIsMissing(bookFlightCommand);
        Location home = convertToLocation(bookFlightCommand.getHome());
        Location destination = convertToLocation(bookFlightCommand.getDestination());
        return new FindAndBookFlightInformation(bookFlightCommand.getTripId(), home, destination,
                bookFlightCommand.getOutboundFlightDate(), bookFlightCommand.getReturnFlightDate(),
                bookFlightCommand.getTravellerName());
    }

    public List<FlightInformationDTO> convertToFlightInformationDTOList(final List<FlightInformation> flightsInformation) throws ConverterException {
        if (flightsInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated flights information could not be " +
                    "received.");
        }

        List<FlightInformationDTO> flightInformationDTOs = new LinkedList<>();

        for (FlightInformation flightInformation : flightsInformation) {
            flightInformationDTOs.add(convertToFlightInformationDTO(flightInformation));
        }

        return flightInformationDTOs;
    }

    public FlightInformationDTO convertToFlightInformationDTO(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included flight information for the received " +
                    "booking is missing.");
        }

        checkIfOneWayTicketOrInformationIsMissing(flightInformation);
        FlightDTO outboundFlight = convertToFlightDTO(flightInformation.getOutboundFlight());
        FlightDTO returnFlight = convertToFlightDTO(flightInformation.getReturnFlight());
        return new FlightInformationDTO(flightInformation.getId(), outboundFlight, returnFlight, flightInformation.getTravellerName(),
                flightInformation.getBookingStatus(), flightInformation.getTripId());
    }

    private Flight convertToFlight(final FlightDTO flightDTO) {
        if (flightDTO == null) {
            return null;
        }

        return new Flight(flightDTO.getCountry(), flightDTO.getFromAirport(), flightDTO.getToAirport(),
                flightDTO.getFlightDate());
    }

    private Location convertToLocation(final LocationDTO locationDTO) throws ConverterException {
        if (locationDTO == null) {
            throw new ConverterException("Information about a location is missing.");
        }

        return new Location(locationDTO.getCountry(), locationDTO.getCity());
    }

    private FlightDTO convertToFlightDTO(final Flight flight) {
        if (flight == null) {
            return null;
        }

        return new FlightDTO(flight.getCountry(), flight.getFromAirport(), flight.getToAirport(),
                flight.getFlightDate());
    }


    private void checkIfOneWayTicketOrInformationIsMissing(final BookFlightCommand bookFlightCommand) throws ConverterException {
        if (bookFlightCommand.getOutboundFlightDate() == null || bookFlightCommand.getReturnFlightDate() == null) {
            throw new ConverterException("Information about a flight date is missing.");
        }
    }

    private void checkIfOneWayTicketOrInformationIsMissing(final BookFlightRequest bookFlightRequest) throws ConverterException {
        if (bookFlightRequest.getOutboundFlight() == null || bookFlightRequest.getReturnFlight() == null) {
            throw new ConverterException("Information about a flight is missing.");
        }
    }

    private void checkIfOneWayTicketOrInformationIsMissing(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }

}
