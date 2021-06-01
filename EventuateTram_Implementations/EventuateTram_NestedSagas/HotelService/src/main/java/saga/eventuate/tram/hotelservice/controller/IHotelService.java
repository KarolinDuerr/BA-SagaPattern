package saga.eventuate.tram.hotelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.RejectionReason;

import java.util.List;

@Qualifier("HotelService")
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

    @Transactional
    void confirmHotelEventBooking(final Long eventBookingId, final Long hotelBookingId);

    @Transactional
    void rejectHotelEventBooking(final Long hotelBookingId, final RejectionReason rejectionReason);
}
