package saga.eventuate.tram.hotelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;

import java.util.List;

@Qualifier("HotelService")
public interface IHotelService {

    List<HotelBooking> getHotelBookings();

    HotelBooking getHotelBooking(Long bookingId) throws HotelException;

    HotelBooking bookHotel(HotelBookingInformation hotelBooking);

    HotelBooking bookHotel(HotelBookingInformation hotelBooking, int tripId);

    boolean cancelHotelBooking(Long bookingId) throws HotelException;
}
