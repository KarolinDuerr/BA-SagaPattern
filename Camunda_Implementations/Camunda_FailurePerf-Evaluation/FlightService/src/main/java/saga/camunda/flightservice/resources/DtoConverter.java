package saga.camunda.flightservice.resources;

import saga.camunda.flightservice.error.FlightException;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.flightservice.api.dto.LocationDTO;
import saga.camunda.flightservice.error.ConverterException;
import saga.camunda.flightservice.error.ErrorType;
import saga.camunda.flightservice.model.FindAndBookFlightInformation;
import saga.camunda.flightservice.model.Flight;
import saga.camunda.flightservice.model.FlightInformation;
import saga.camunda.flightservice.model.Location;
import saga.camunda.flightservice.model.dto.FlightDTO;
import saga.camunda.flightservice.model.dto.FlightInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public FindAndBookFlightInformation convertToFindAndBookFlightInformation(final BookFlightRequest bookFlightRequest) throws ConverterException, FlightException {
        if (bookFlightRequest == null) {
            throw new ConverterException("The information to find and book a flight is missing.");
        }

        checkIfInformationIsMissing(bookFlightRequest);
        Location home = convertToLocation(bookFlightRequest.getHome());
        Location destination = convertToLocation(bookFlightRequest.getDestination());
        return new FindAndBookFlightInformation(bookFlightRequest.getTripId(), home, destination,
                bookFlightRequest.getOutboundFlightDate(), bookFlightRequest.getReturnFlightDate(),
                bookFlightRequest.getTravellerName());
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

    private void checkIfInformationIsMissing(final BookFlightRequest bookFlightRequest) throws ConverterException {
        if (bookFlightRequest.getOutboundFlightDate() == null || bookFlightRequest.getReturnFlightDate() == null) {
            throw new ConverterException("Information about a flight date is missing.");
        }

        if (bookFlightRequest.getTravellerName() == null || bookFlightRequest.getTravellerName().isEmpty()) {
            throw new ConverterException("Traveller name information about a flight is missing.");
        }
    }

    private void checkIfInformationIsMissing(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }
}
