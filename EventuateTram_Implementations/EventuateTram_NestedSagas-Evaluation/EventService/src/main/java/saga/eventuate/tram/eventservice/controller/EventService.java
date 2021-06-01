package saga.eventuate.tram.eventservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.eventservice.error.BookingNotFound;
import saga.eventuate.tram.eventservice.error.ErrorType;
import saga.eventuate.tram.eventservice.error.EventException;
import saga.eventuate.tram.eventservice.model.*;

import java.util.*;

@Service("EventService")
@Transactional
public class EventService implements IEventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private final EventBookingRepository eventBookingRepository;

    @Autowired
    private final EventRepository eventRepository;

    public EventService(final EventBookingRepository eventBookingRepository, final EventRepository eventRepository) {
        this.eventBookingRepository = eventBookingRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getEvents() {
        logger.info("Get events from Repository.");

        List<Event> events = new LinkedList<>();

        Iterable<Event> existingEvents = eventRepository.findAll();

        for (Event booking : existingEvents) {
            events.add(booking);
        }

        return events;
    }

    @Override
    public Event getEvent(final Long id) throws EventException {
        logger.info(String.format("Get event (ID: %d) from Repository.", id));

        Optional<Event> event = eventRepository.findById(id);

        if (!event.isPresent()) {
            String message = String.format("The event (ID: %d) does not exist.", id);
            logger.info(message);
            throw new EventException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return event.get();
    }

    @Override
    public List<EventBooking> getEventBookings() {
        logger.info("Get event bookings from Repository.");

        List<EventBooking> eventBookings = new LinkedList<>();

        Iterable<EventBooking> bookings = eventBookingRepository.findAll();

        for (EventBooking booking : bookings) {
            eventBookings.add(booking);
        }

        return eventBookings;
    }

    @Override
    public EventBooking getEventBooking(final Long bookingId) throws EventException {
        logger.info(String.format("Get event booking (ID: %d) from Repository.", bookingId));

        Optional<EventBooking> eventBooking = eventBookingRepository.findById(bookingId);

        if (!eventBooking.isPresent()) {
            String message = String.format("The event booking (ID: %d) does not exist.", bookingId);
            logger.info(message);
            throw new EventException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return eventBooking.get();
    }

    @Override
    public EventBooking bookEvent(final String travellerName, final Long eventId, final Long hotelBookingId) throws EventException {
        logger.info("Saving the booked Event with ID: " + eventId);

        EventBooking newEventBooking = bookAvailableEvent(travellerName, eventId, hotelBookingId);

        // no hotelBooking assigned therefore the booking has already been confirmed
        if (newEventBooking.getHotelBookingId() == -1) {
            newEventBooking.confirm();
        }

        // ensure idempotence of event bookings
        EventBooking alreadyExistingEventBooking = checkIfBookingAlreadyExists(newEventBooking);
        if (alreadyExistingEventBooking != null) {
            return alreadyExistingEventBooking;
        }

        eventBookingRepository.save(newEventBooking);

        return newEventBooking;
    }

    @Override
    public void cancelEventBooking(final Long bookingId, final Long hotelBookingId) {
        logger.info("Cancelling the booked event with ID " + bookingId);

        try {
            EventBooking eventBooking = getEventBooking(bookingId);

            if (eventBooking.getHotelBookingId() != hotelBookingId) {
                throw new BookingNotFound(bookingId);
            }

            eventBooking.cancel(BookingStatus.CANCELLED);
            eventBookingRepository.save(eventBooking);
        } catch (EventException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    @Override
    public void confirmEventBooking(final Long bookingId, final Long hotelBookingId) {
        logger.info("Confirming the booked event with ID " + bookingId);

        try {
            EventBooking eventBooking = getEventBooking(bookingId);

            if (eventBooking.getHotelBookingId() != hotelBookingId) {
                throw new BookingNotFound(bookingId);
            }

            eventBooking.confirm();
            eventBookingRepository.save(eventBooking);
        } catch (EventException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    // only mocking the general function of this method
    private EventBooking bookAvailableEvent(final String travellerName, final Long eventId,
                                            final Long hotelBookingId) throws EventException {
        if (eventId < 0) {
            logger.info("Provoked event exception: no available space in event for hotel booking: " + hotelBookingId);
            throw new EventException(ErrorType.NO_AVAILABLE_SPACE,
                    "No available space in event with ID " + eventId + " found.");
        }

        Optional<Event> bookedEvent = eventRepository.findById(eventId);
        if (!bookedEvent.isPresent()) {
            String message = String.format("The event (ID: %d) does not exist.", eventId);
            logger.info(message);
            throw new EventException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return new EventBooking(hotelBookingId, travellerName, bookedEvent.get());
    }

    // ensure idempotence of event bookings
    private EventBooking checkIfBookingAlreadyExists(final EventBooking eventBooking) {
        List<EventBooking> customerBookedEvents =
                eventBookingRepository.findByTravellerName(eventBooking.getTravellerName());

        Optional<EventBooking> savedEventBooking =
                customerBookedEvents.stream().filter(eventBookingInfo -> eventBookingInfo.equals(eventBooking)).findFirst();

        if (!savedEventBooking.isPresent()) {
            return null;
        }

        logger.info("Event has already been booked: " + savedEventBooking.toString());
        return savedEventBooking.get();
    }

    @Override
    // to provide information which can be shown as no events can be registered in the example application
    public void provideExampleEntries() {
        Address maxAddress = new Address("Germany", "Bamberg", "Kapellenstraße", 13);
        Date maxDate = new GregorianCalendar(2021, Calendar.DECEMBER, 5).getTime();
        Event maxEvent = new Event("Bowling", maxDate, maxAddress, 8L, 15.00);

        Address alexAddress = new Address("Scotland", "Stirling", "Hermitage Road", 9);
        Date alexDate = new GregorianCalendar(2021, Calendar.DECEMBER, 5).getTime();
        Event alexEvent = new Event("Minigolf", alexDate, alexAddress, 4L, 12.00);

        Address peterAddress = new Address("USA", "New York", "10th Ave, Brooklyn", 1603);
        Date peterDate = new GregorianCalendar(2021, Calendar.DECEMBER, 5).getTime();
        Event peterEvent = new Event("Amusement park", peterDate, peterAddress, 200L, 25.00);

        eventRepository.save(maxEvent);
        eventRepository.save(alexEvent);
        eventRepository.save(peterEvent);
    }
}
