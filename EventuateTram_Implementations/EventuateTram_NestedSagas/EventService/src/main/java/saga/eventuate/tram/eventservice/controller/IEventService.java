package saga.eventuate.tram.eventservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.eventservice.error.EventException;
import saga.eventuate.tram.eventservice.model.Event;
import saga.eventuate.tram.eventservice.model.EventBooking;

import java.util.List;

@Qualifier("EventService")
public interface IEventService {

    @Transactional
    List<Event> getEvents();

    @Transactional
    Event getEvent(final Long bookingId) throws EventException;

    @Transactional
    List<EventBooking> getEventBookings();

    @Transactional
    EventBooking getEventBooking(final Long bookingId) throws EventException;

    @Transactional
    EventBooking bookEvent(final String travellerName, final Long eventId, final Long hotelBookingId) throws EventException;

    @Transactional
    void cancelEventBooking(final Long bookingId, final Long hotelBookingId);

    @Transactional
    void confirmEventBooking(final Long bookingId, final Long hotelBookingId);

    // to have example entries in the database
    void provideExampleEntries();
}
