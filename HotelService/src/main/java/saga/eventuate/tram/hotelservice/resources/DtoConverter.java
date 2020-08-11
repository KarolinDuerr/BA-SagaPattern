package saga.eventuate.tram.hotelservice.resources;

import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.hotelservice.error.ConverterException;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.StayDuration;

public class DtoConverter {

    public static final DtoConverter instance = new DtoConverter();

    public HotelBookingInformation convertToHotelBooking(BookHotelRequest bookHotelRequest) throws ConverterException {
        if (bookHotelRequest == null) {
            throw new ConverterException("The information to book a hotel is missing.");
        }

        return new HotelBookingInformation(bookHotelRequest.getCountry(), bookHotelRequest.getCity(), convertToStayDuration(bookHotelRequest.getDuration()),
                bookHotelRequest.getNumberOfPersons(), bookHotelRequest.getNumberOfRooms());
    }

    public StayDuration convertToStayDuration(StayDurationDTO stayDurationDTO) throws ConverterException {
        if (stayDurationDTO == null) {
            throw new ConverterException("The duration for the stay is missing.");
        }

        if (stayDurationDTO.getStartDate() == null || stayDurationDTO.getEndDate() == null) {
            throw new ConverterException("The duration start or end date for the stay is missing.");
        }

        return new StayDuration(stayDurationDTO.getStartDate(), stayDurationDTO.getEndDate());
    }
}
