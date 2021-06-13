package saga.microprofile.flightservice.resources;

import saga.microprofile.flightservice.api.dto.BookFlightRequest;
import saga.microprofile.flightservice.api.dto.LocationDTO;
import saga.microprofile.flightservice.error.ConverterException;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.FlightException;
import saga.microprofile.flightservice.model.FindAndBookFlightInformation;
import saga.microprofile.flightservice.model.Flight;
import saga.microprofile.flightservice.model.FlightInformation;
import saga.microprofile.flightservice.model.Location;
import saga.microprofile.flightservice.model.dto.FlightDTO;
import saga.microprofile.flightservice.model.dto.FlightInformationDTO;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class DtoConverter {

    public FindAndBookFlightInformation convertToFindAndBookFlightInformation(final URI lraId,
                                                                              final BookFlightRequest bookFlightRequest) throws ConverterException, FlightException {
        if (bookFlightRequest == null) {
            throw new ConverterException("The information to find and book a flight is missing.");
        }

        checkIfInformationIsMissing(bookFlightRequest);
        Location home = convertToLocation(bookFlightRequest.getHome());
        Location destination = convertToLocation(bookFlightRequest.getDestination());

        ZoneId zoneId = ZoneId.systemDefault();
        Date outboundFlightDate = Date.from(bookFlightRequest.getOutboundFlightDate().atStartOfDay(zoneId).toInstant());
        Date returnFlightDate = Date.from(bookFlightRequest.getReturnFlightDate().atStartOfDay(zoneId).toInstant());

        return new FindAndBookFlightInformation(bookFlightRequest.getTripId(), lraId, home, destination,
                outboundFlightDate, returnFlightDate, bookFlightRequest.getTravellerName());
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

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime flightDate = flight.getFlightDate().toInstant().atZone(zoneId).toLocalDateTime();

        return new FlightDTO(flight.getCountry(), flight.getFromAirport(), flight.getToAirport(), flightDate);
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
