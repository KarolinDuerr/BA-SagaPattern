package saga.netflix.conductor.flightservice.resources;

import saga.netflix.conductor.flightservice.error.ConverterException;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightException;
import saga.netflix.conductor.flightservice.model.Flight;
import saga.netflix.conductor.flightservice.model.FlightInformation;
import saga.netflix.conductor.flightservice.model.dto.BookFlightRequest;
import saga.netflix.conductor.flightservice.model.dto.FlightDTO;
import saga.netflix.conductor.flightservice.model.dto.FlightInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public FlightInformation convertToFlightInformation(final BookFlightRequest bookFlightRequest) throws ConverterException, FlightException {
        if (bookFlightRequest == null) {
            throw new ConverterException("The information to book the flight is missing.");
        }

        checkIfInformationIsMissing(bookFlightRequest);
        Flight outboundFlight = convertToFlight(bookFlightRequest.getOutboundFlight());
        Flight returnFlight = convertToFlight(bookFlightRequest.getReturnFlight());
        return new FlightInformation(outboundFlight, returnFlight, bookFlightRequest.getTravellerName());
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

    private Flight convertToFlight(final FlightDTO flightDTO) {
        if (flightDTO == null) {
            return null;
        }

        return new Flight(flightDTO.getCountry(), flightDTO.getFromAirport(), flightDTO.getToAirport(),
                flightDTO.getFlightDate());
    }

    private FlightDTO convertToFlightDTO(final Flight flight) {
        if (flight == null) {
            return null;
        }

        return new FlightDTO(flight.getCountry(), flight.getFromAirport(), flight.getToAirport(),
                flight.getFlightDate());
    }

    private void checkIfInformationIsMissing(final BookFlightRequest bookFlightRequest) throws ConverterException {
        if (bookFlightRequest.getOutboundFlight() == null || bookFlightRequest.getReturnFlight() == null) {
            throw new ConverterException("Information about a flight is missing.");
        }

        if (bookFlightRequest.getTravellerName() == null || bookFlightRequest.getTravellerName().isEmpty()) {
            throw new ConverterException("Information about a flight is missing.");
        }
    }

    private void checkIfInformationIsMissing(final FlightInformation flightInformation) throws ConverterException {
        if (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }
}
