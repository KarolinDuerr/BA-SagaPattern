package saga.eventuate.tram.flightservice.resources;

import saga.eventuate.tram.flightservice.api.dto.BookFlightRequest;
import saga.eventuate.tram.flightservice.api.dto.FlightDTO;
import saga.eventuate.tram.flightservice.error.ConverterException;
import saga.eventuate.tram.flightservice.error.ErrorType;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.Flight;
import saga.eventuate.tram.flightservice.model.FlightInformation;
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
        return new FlightInformation(outboundFlight, returnFlight, bookFlightRequest.getOneWay(),
                bookFlightRequest.getTravellerName());
    }

    public Flight convertToFlight(final FlightDTO flightDTO) throws FlightException {
        if (flightDTO == null) {
            return null;
        }

        return new Flight(flightDTO.getCountry(), flightDTO.getFromAirport(), flightDTO.getToAirport(),
                flightDTO.getFlightDateDeparture(), flightDTO.getFlightDateArrival(), flightDTO.getSeatNumber());
    }

    public List<FlightInformationDTO> convertToFlightInformationDTOList(List<FlightInformation> flightsInformation) throws ConverterException {
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
        return new FlightInformationDTO(flightInformation.getId(), outboundFlight, returnFlight,
                flightInformation.getOneWay(), flightInformation.getTravellerName(),
                flightInformation.getBookingStatus());
    }

    public FlightDTO convertToFlightDTO(final Flight flight) {
        if (flight == null) {
            return null;
        }

        return new FlightDTO(flight.getCountry(), flight.getFromAirport(), flight.getToAirport(),
                flight.getFlightDateDeparture(), flight.getFlightDateArrival(), flight.getSeatNumber());
    }

    private void checkIfOneWayTicketOrInformationIsMissing(BookFlightRequest bookFlightRequest) throws ConverterException {
        if (!bookFlightRequest.getOneWay() && (bookFlightRequest.getOutboundFlight() == null || bookFlightRequest.getReturnFlight() == null)) {
            throw new ConverterException("Information about a flight is missing.");
        }
    }

    private void checkIfOneWayTicketOrInformationIsMissing(FlightInformation flightInformation) throws ConverterException {
        if (!flightInformation.getOneWay() && (flightInformation.getOutboundFlight() == null || flightInformation.getReturnFlight() == null)) {
            throw new ConverterException("Information for an included flight is missing.");
        }
    }

}
