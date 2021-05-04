package saga.microprofile.hotelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.microprofile.hotelservice.error.HotelException;
import saga.microprofile.hotelservice.model.HotelBooking;
import saga.microprofile.hotelservice.model.HotelBookingInformation;

import java.util.List;

@Qualifier("HotelService")
public interface IHotelService {

    List<HotelBooking> getHotelBookings();

    HotelBooking getHotelBooking(final Long bookingId) throws HotelException;

    HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException;

    void cancelHotelBooking(final long tripId, final String travellerName) ;

    void confirmHotelBooking(final Long bookingId, final Long tripId);
}
