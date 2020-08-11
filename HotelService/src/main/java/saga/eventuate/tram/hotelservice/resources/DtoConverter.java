package saga.eventuate.tram.hotelservice.resources;

import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.hotelservice.error.ConverterException;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.StayDuration;
import saga.eventuate.tram.hotelservice.model.dto.HotelBookingDTO;
import saga.eventuate.tram.hotelservice.model.dto.HotelBookingInformationDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public HotelBookingInformation convertToHotelBookingInformation(BookHotelRequest bookHotelRequest) throws ConverterException, HotelException {
        if (bookHotelRequest == null) {
            throw new ConverterException("The information to book a hotel is missing.");
        }

        return new HotelBookingInformation(bookHotelRequest.getCountry(), bookHotelRequest.getCity(),
                convertToStayDuration(bookHotelRequest.getDuration()),
                bookHotelRequest.getNumberOfPersons(), bookHotelRequest.getNumberOfRooms());
    }

    public StayDuration convertToStayDuration(StayDurationDTO stayDurationDTO) throws ConverterException, HotelException {
        if (stayDurationDTO == null) {
            throw new ConverterException("The duration for the stay is missing.");
        }

        if (stayDurationDTO.getArrvival() == null || stayDurationDTO.getDeparture() == null) {
            throw new ConverterException("The duration start or end date for the stay is missing.");
        }

        return new StayDuration(stayDurationDTO.getArrvival(), stayDurationDTO.getDeparture());
    }

    public List<HotelBookingDTO> convertToHotelBookingDTOList(List<HotelBooking> hotelBookings) throws ConverterException {
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

    public HotelBookingDTO convertToHotelBookingDTO(HotelBooking hotelBooking) throws ConverterException {
        if (hotelBooking == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated hotel booking could not be received" +
                    "." +
                    ".");
        }

        return new HotelBookingDTO(hotelBooking.getHotelName(),
                convertToHotelBookingInformationDTO(hotelBooking.getBookingInformation()));
    }

    public HotelBookingInformationDTO convertToHotelBookingInformationDTO(HotelBookingInformation hotelBookingInformation) throws ConverterException {
        if (hotelBookingInformation == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included hotel information for the received " +
                    "booking is missing.");
        }

        return new HotelBookingInformationDTO(hotelBookingInformation.getCountry(), hotelBookingInformation.getCity()
                , convertToStayDurationDTO(hotelBookingInformation.getDuration()),
                hotelBookingInformation.getNumberOfPersons(), hotelBookingInformation.getNumberOfRooms());
    }

    public StayDurationDTO convertToStayDurationDTO(StayDuration stayDuration) throws ConverterException {
        if (stayDuration == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The stay duration for the received hotel booking " +
                    "is missing.");
        }

        return new StayDurationDTO(stayDuration.getArrival(), stayDuration.getDeparture());
    }
}
