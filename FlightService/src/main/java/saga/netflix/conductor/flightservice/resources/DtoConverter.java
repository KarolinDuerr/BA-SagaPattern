package saga.netflix.conductor.flightservice.resources;

import saga.netflix.conductor.flightservice.error.ConverterException;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.model.FindAndBookFlightInformation;
import saga.netflix.conductor.flightservice.model.Flight;
import saga.netflix.conductor.flightservice.model.FlightInformation;
import saga.netflix.conductor.flightservice.model.Location;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightTask;
import saga.netflix.conductor.flightservice.model.dto.FlightDTO;
import saga.netflix.conductor.flightservice.model.dto.FlightInformationDTO;
import saga.netlfix.conductor.flightservice.api.dto.LocationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public FindAndBookFlightInformation convertToFindAndBookFlightInformation(final BookFlightTask bookFlightTask) throws ConverterException, FlightException {
        if (bookFlightTask == null) {
            throw new ConverterException("The information to find and book a flight is missing.");
        }

        checkIfInformationIsMissing(bookFlightTask);
        Location home = convertToLocation(bookFlightTask.getHome());
        Location destination = convertToLocation(bookFlightTask.getDestination());
        return new FindAndBookFlightInformation(bookFlightTask.getTripId(), home, destination,
                bookFlightTask.getOutboundFlightDate(), bookFlightTask.getReturnFlightDate(),
                bookFlightTask.getTravellerName());
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
        return new FlightInformationDTO(flightInformation.getId(), outboundFlight, returnFlight, flightInformation.getTravellerName(),
                flightInformation.getBookingStatus(), flightInformation.getTripId());
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

    private void checkIfInformationIsMissing(final BookFlightTask bookFlightTask) throws ConverterException {
        if (bookFlightTask.getOutboundFlightDate() == null || bookFlightTask.getReturnFlightDate() == null) {
            throw new ConverterException("Information about a flight date is missing.");
        }

        if (bookFlightTask.getTravellerName() == null || bookFlightTask.getTravellerName().isEmpty()) {
            throw new ConverterException("Information about a flight is missing.");
        }
    }

    private void checkIfInformationIsMissing(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }
}
