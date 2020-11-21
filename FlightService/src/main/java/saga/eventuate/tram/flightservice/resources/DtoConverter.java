package saga.eventuate.tram.flightservice.resources;

import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.model.dto.FlightDTO;
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

    public FindAndBookFlightInformation convertToFindAndBookFlightInformation(final BookFlightCommand bookFlightCommand) throws ConverterException, FlightException {
        if (bookFlightCommand == null) {
            throw new ConverterException("The information to find and book a flight is missing.");
        }

        checkIfInformationIsMissing(bookFlightCommand);
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

        checkIfInformationIsMissing(flightInformation);
        FlightDTO outboundFlight = convertToFlightDTO(flightInformation.getOutboundFlight());
        FlightDTO returnFlight = convertToFlightDTO(flightInformation.getReturnFlight());
        return new FlightInformationDTO(flightInformation.getId(), outboundFlight, returnFlight,
                flightInformation.getTravellerName(),
                flightInformation.getBookingStatus(), flightInformation.getTripId());
    }

    private Location convertToLocation(final LocationDTO locationDTO) throws ConverterException {
        if (locationDTO == null) {
            throw new ConverterException("Information about a location is missing.");
        }

        return new Location(locationDTO.getCountry(), locationDTO.getCity());
    }

    private FlightDTO convertToFlightDTO(final Flight flight) throws ConverterException {
        if (flight == null) {
            throw new ConverterException("Information about a flight is missing.");
        }

        return new FlightDTO(flight.getCountry(), flight.getFromAirport(), flight.getToAirport(),
                flight.getFlightDate());
    }

    private void checkIfInformationIsMissing(final BookFlightCommand bookFlightCommand) throws ConverterException {
        if (bookFlightCommand.getOutboundFlightDate() == null || bookFlightCommand.getReturnFlightDate() == null) {
            throw new ConverterException("Information about a flight date is missing.");
        }

        if (bookFlightCommand.getTravellerName() == null || bookFlightCommand.getTravellerName().isEmpty()) {
            throw new ConverterException("Traveller name information about a flight is missing.");
        }
    }

    private void checkIfInformationIsMissing(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }
}
