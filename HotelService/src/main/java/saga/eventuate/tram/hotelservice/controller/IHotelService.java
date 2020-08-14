package saga.eventuate.tram.hotelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("HotelService")
public interface IHotelService {

    @Transactional
    List<HotelBooking> getHotelBookings();

    @Transactional
    HotelBooking getHotelBooking(final Long bookingId) throws HotelException;

    @Transactional
    HotelBooking bookHotel(final HotelBookingInformation hotelBooking);

    @Transactional
    boolean cancelHotelBooking(final Long bookingId) throws HotelException;

    @Transactional
    void cancelHotelBooking(final Long bookingId, final Long tripId);

    @Transactional
    void confirmHotelBooking(final Long bookingId, final Long tripId);
}
