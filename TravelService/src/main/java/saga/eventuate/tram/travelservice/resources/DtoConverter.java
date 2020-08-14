package saga.eventuate.tram.travelservice.resources;

import saga.eventuate.tram.travelservice.api.dto.BookTripRequest;
import saga.eventuate.tram.travelservice.api.dto.LocationDTO;
import saga.eventuate.tram.travelservice.api.dto.TripDurationDTO;
import saga.eventuate.tram.travelservice.error.ConverterException;
import saga.eventuate.tram.travelservice.error.ErrorType;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.Location;
import saga.eventuate.tram.travelservice.model.TripDuration;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.model.dto.TripInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public TripInformation convertToTripInformation(BookTripRequest bookTripRequest) throws ConverterException,
            TravelException {
        if (bookTripRequest == null) {
            throw new ConverterException("The information to book a trip is missing.");
        }

        TripDuration tripDuration = convertToTripDuration(bookTripRequest.getDuration());
        Location start = convertToLocation(bookTripRequest.getStart());
        Location destination = convertToLocation(bookTripRequest.getDestination());
        return new TripInformation(tripDuration, start, destination, bookTripRequest.getTravellerNames(),
                bookTripRequest.getNumberOfPersons(), bookTripRequest.getNumberOfRooms(),
                bookTripRequest.getOneWayFlight(), bookTripRequest.getCustomerId());
    }

    public TripDuration convertToTripDuration(TripDurationDTO tripDurationDTO) throws ConverterException,
            TravelException {
        if (tripDurationDTO == null) {
            throw new ConverterException("The duration of the trip is missing.");
        }

        return new TripDuration(tripDurationDTO.getStart(), tripDurationDTO.getEnd());
    }

    public Location convertToLocation(LocationDTO locationDTO) throws ConverterException {
        if (locationDTO == null) {
            throw new ConverterException("The location information of the trip is missing.");
        }

        return new Location(locationDTO.getCountry(), locationDTO.getCity());
    }

    public List<TripInformationDTO> convertToTripInformationDTOList(List<TripInformation> tripsInformation) throws ConverterException {
        if (tripsInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated trips information could not be " +
                    "received.");
        }

        List<TripInformationDTO> tripsInformationDTOs = new LinkedList<>();

        for (TripInformation tripInformation : tripsInformation) {
            tripsInformationDTOs.add(convertToTripInformationDTO(tripInformation));
        }

        return tripsInformationDTOs;
    }

    public TripInformationDTO convertToTripInformationDTO(TripInformation tripInformation) throws ConverterException {
        if (tripInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated trip booking could not be " +
                    "received.");
        }

        TripDurationDTO tripDuration = convertToTripDurationDTO(tripInformation.getDuration());
        LocationDTO start = convertToLocationDTO(tripInformation.getStart());
        LocationDTO destination = convertToLocationDTO(tripInformation.getDestination());
        return new TripInformationDTO(tripInformation.getId(), tripDuration, start, destination,
                tripInformation.getTravellerNames(), tripInformation.getNumberOfPersons(),
                tripInformation.getNumberOfRooms(), tripInformation.getOneWayFlight(),
                tripInformation.getCustomerId(), tripInformation.getBookingStatus(), tripInformation.getHotelId(),
                tripInformation.getFlightId());
    }

    public TripDurationDTO convertToTripDurationDTO(TripDuration tripDuration) throws ConverterException {
        if (tripDuration == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included trip duration could not be received.");
        }

        return new TripDurationDTO(tripDuration.getStart(), tripDuration.getEnd());
    }

    public LocationDTO convertToLocationDTO(Location location) throws ConverterException {
        if (location == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included location could not be received.");
        }

        return new LocationDTO(location.getCountry(), location.getCity());
    }
}
