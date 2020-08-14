package saga.eventuate.tram.hotelservice.resources;

import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.hotelservice.error.ConverterException;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.Destination;
import saga.eventuate.tram.hotelservice.model.StayDuration;
import saga.eventuate.tram.hotelservice.model.dto.HotelBookingDTO;
import saga.eventuate.tram.hotelservice.model.dto.HotelBookingInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public HotelBookingInformation convertToHotelBookingInformation(final BookHotelRequest bookHotelRequest) throws ConverterException, HotelException {
        if (bookHotelRequest == null) {
            throw new ConverterException("The information to book a hotel is missing.");
        }

        return new HotelBookingInformation(bookHotelRequest.getTripId(),
                convertToDestination(bookHotelRequest.getDestination()),
                convertToStayDuration(bookHotelRequest.getDuration()), bookHotelRequest.getNumberOfPersons(),
                bookHotelRequest.getNumberOfRooms());
    }

    public List<HotelBookingDTO> convertToHotelBookingDTOList(final List<HotelBooking> hotelBookings) throws ConverterException {
        if (hotelBookings == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated hotel bookings could not be " +
                    "received.");
        }

        List<HotelBookingDTO> hotelBookingDTOs = new LinkedList<>();

        for (HotelBooking hotelBooking : hotelBookings) {
            hotelBookingDTOs.add(convertToHotelBookingDTO(hotelBooking));
        }

        return hotelBookingDTOs;
    }

    public HotelBookingDTO convertToHotelBookingDTO(final HotelBooking hotelBooking) throws ConverterException {
        if (hotelBooking == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated hotel booking could not be received" +
                    ".");
        }

        return new HotelBookingDTO(hotelBooking.getId(), hotelBooking.getHotelName(),
                convertToHotelBookingInformationDTO(hotelBooking.getBookingInformation()),
                hotelBooking.getBookingStatus());
    }

    public HotelBookingInformationDTO convertToHotelBookingInformationDTO(final HotelBookingInformation hotelBookingInformation) throws ConverterException {
        if (hotelBookingInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included hotel information for the received " +
                    "booking is missing.");
        }

        return new HotelBookingInformationDTO(convertToDestinationDTO(hotelBookingInformation.getDestination()),
                convertToStayDurationDTO(hotelBookingInformation.getDuration()),
                hotelBookingInformation.getNumberOfPersons(), hotelBookingInformation.getNumberOfRooms(),
                hotelBookingInformation.getTripId());
    }

    private Destination convertToDestination(final DestinationDTO destinationDTO) throws ConverterException {
        if (destinationDTO == null) {
            throw new ConverterException("The destination for the stay is missing.");
        }

        return new Destination(destinationDTO.getCountry(), destinationDTO.getCity());
    }

    private StayDuration convertToStayDuration(final StayDurationDTO stayDurationDTO) throws ConverterException,
            HotelException {
        if (stayDurationDTO == null) {
            throw new ConverterException("The duration for the stay is missing.");
        }

        if (stayDurationDTO.getArrival() == null || stayDurationDTO.getDeparture() == null) {
            throw new ConverterException("The duration start or end date for the stay is missing.");
        }

        return new StayDuration(stayDurationDTO.getArrival(), stayDurationDTO.getDeparture());
    }

    private DestinationDTO convertToDestinationDTO(final Destination destination) throws ConverterException {
        if (destination == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The destination for the received hotel booking is" +
                    " missing.");
        }

        return new DestinationDTO(destination.getCountry(), destination.getCity());
    }

    private StayDurationDTO convertToStayDurationDTO(final StayDuration stayDuration) throws ConverterException {
        if (stayDuration == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The stay duration for the received hotel booking " +
                    "is missing.");
        }

        return new StayDurationDTO(stayDuration.getArrival(), stayDuration.getDeparture());
    }
}
