package saga.eventuate.tram.eventservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import saga.eventuate.tram.eventservice.controller.IEventService;
import saga.eventuate.tram.eventservice.error.EventServiceException;
import saga.eventuate.tram.eventservice.model.Event;
import saga.eventuate.tram.eventservice.model.EventBooking;
import saga.eventuate.tram.eventservice.model.dto.EventBookingDTO;
import saga.eventuate.tram.eventservice.model.dto.EventDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
public class EventResource {

    private static final Logger logger = LoggerFactory.getLogger(EventResource.class);

    @Autowired
    private IEventService eventService;

    @Autowired
    private DtoConverter dtoConverter;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() throws EventServiceException {
        logger.info("Get events.");

        List<Event> events = eventService.getEvents();

        if (events == null) {
            logger.info("Something went wrong during receiving the events.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the events.");
        }

        return ResponseEntity.ok(dtoConverter.convertToEventDTOList(events));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable(value = "eventId") final Long eventId) throws EventServiceException {
        logger.info("Get event with ID: " + eventId);

        Event event = eventService.getEvent(eventId);

        if (event == null) {
            logger.info("Something went wrong during receiving the event.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the event.");
        }

        return ResponseEntity.ok(dtoConverter.convertToEventDTO(event));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<EventBookingDTO>> getEventBookings() throws EventServiceException {
        logger.info("Get event bookings.");

        List<EventBooking> eventBookings = eventService.getEventBookings();

        if (eventBookings == null) {
            logger.info("Something went wrong during receiving the event bookings.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the event bookings.");
        }

        return ResponseEntity.ok(dtoConverter.convertToEventBookingDTOList(eventBookings));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<EventBookingDTO> getEventBooking(@PathVariable(value = "bookingId") final Long bookingId) throws EventServiceException {
        logger.info("Get event booking with ID: " + bookingId);

        EventBooking hotelBooking = eventService.getEventBooking(bookingId);

        if (hotelBooking == null) {
            logger.info("Something went wrong during receiving the event booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the event booking.");
        }

        return ResponseEntity.ok(dtoConverter.convertToEventBookingDTO(hotelBooking));
    }
}
