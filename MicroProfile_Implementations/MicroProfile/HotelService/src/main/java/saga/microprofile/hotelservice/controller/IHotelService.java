package saga.microprofile.hotelservice.controller;

import saga.microprofile.hotelservice.error.HotelException;
import saga.microprofile.hotelservice.model.HotelBooking;
import saga.microprofile.hotelservice.model.HotelBookingInformation;

import javax.transaction.Transactional;
import java.util.List;

public interface IHotelService {

    @Transactional
    List<HotelBooking> getHotelBookings();

    @Transactional
    HotelBooking getHotelBooking(final Long bookingId) throws HotelException;

    @Transactional
    HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException;

    @Transactional
    void cancelHotelBooking(final Long bookingId, final Long tripId);

    @Transactional
    void confirmHotelBooking(final Long bookingId, final Long tripId);
}
