package saga.microprofile.travelservice.resources;

import saga.microprofile.travelservice.api.dto.BookTripRequest;
import saga.microprofile.travelservice.api.dto.LocationDTO;
import saga.microprofile.travelservice.api.dto.TripDurationDTO;
import saga.microprofile.travelservice.error.ConverterException;
import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelException;
import saga.microprofile.travelservice.model.Location;
import saga.microprofile.travelservice.model.TripDuration;
import saga.microprofile.travelservice.model.TripInformation;
import saga.microprofile.travelservice.model.dto.TripInformationDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class DtoConverter {

    public TripInformation convertToTripInformation(final String lraId, final BookTripRequest bookTripRequest) throws ConverterException,
            TravelException {
        if (bookTripRequest == null) {
            throw new ConverterException("The information to book a trip is missing.");
        }

        TripDuration tripDuration = convertToTripDuration(bookTripRequest.getDuration());
        Location start = convertToLocation(bookTripRequest.getStart());
        Location destination = convertToLocation(bookTripRequest.getDestination());
        return new TripInformation(lraId, tripDuration, start, destination, bookTripRequest.getTravellerName(),
                bookTripRequest.getBoardType(), bookTripRequest.getCustomerId());
    }

    public List<TripInformationDTO> convertToTripInformationDTOList(final List<TripInformation> tripsInformation) throws ConverterException {
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

    public TripInformationDTO convertToTripInformationDTO(final TripInformation tripInformation) throws ConverterException {
        if (tripInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated trip booking could not be " +
                    "received.");
        }

        TripDurationDTO tripDuration = convertToTripDurationDTO(tripInformation.getDuration());
        LocationDTO start = convertToLocationDTO(tripInformation.getStart());
        LocationDTO destination = convertToLocationDTO(tripInformation.getDestination());
        return new TripInformationDTO(tripInformation.getId(), tripDuration, start, destination,
                tripInformation.getTravellerName(), tripInformation.getBoardType(), tripInformation.getCustomerId(),
                tripInformation.getBookingStatus(), tripInformation.getHotelId(), tripInformation.getFlightId());
    }

    private TripDuration convertToTripDuration(final TripDurationDTO tripDurationDTO) throws ConverterException,
            TravelException {
        if (tripDurationDTO == null) {
            throw new ConverterException("The duration of the trip is missing.");
        }

//        LocalDate start = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(tripDurationDTO.getStart()));
//        LocalDate end = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(tripDurationDTO.getEnd()));
        return new TripDuration(tripDurationDTO.getStart(), tripDurationDTO.getEnd());
    }

    private Location convertToLocation(final LocationDTO locationDTO) throws ConverterException {
        if (locationDTO == null) {
            throw new ConverterException("The location information of the trip is missing.");
        }

        return new Location(locationDTO.getCountry(), locationDTO.getCity());
    }

    private TripDurationDTO convertToTripDurationDTO(final TripDuration tripDuration) throws ConverterException {
        if (tripDuration == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included trip duration could not be received.");
        }

        return new TripDurationDTO(tripDuration.getStart(), tripDuration.getEnd());
    }

    private LocationDTO convertToLocationDTO(final Location location) throws ConverterException {
        if (location == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included location could not be received.");
        }

        return new LocationDTO(location.getCountry(), location.getCity());
    }
}
