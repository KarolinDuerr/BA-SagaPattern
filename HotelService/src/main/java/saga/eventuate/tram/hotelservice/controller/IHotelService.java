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

    boolean cancelHotelBooking(Long bookingId) throws HotelException;

    void cancelHotelBooking(Long bookingId, Long tripId);

    void confirmHotelBooking(Long bookingId, Long tripId);
}
