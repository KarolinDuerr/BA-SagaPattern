package saga.eventuate.tram.eventservice.resources;

import saga.eventuate.tram.eventservice.error.ConverterException;
import saga.eventuate.tram.eventservice.error.ErrorType;
import saga.eventuate.tram.eventservice.model.Address;
import saga.eventuate.tram.eventservice.model.Event;
import saga.eventuate.tram.eventservice.model.EventBooking;
import saga.eventuate.tram.eventservice.model.dto.AddressDTO;
import saga.eventuate.tram.eventservice.model.dto.EventBookingDTO;
import saga.eventuate.tram.eventservice.model.dto.EventDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public List<EventDTO> convertToEventDTOList(final List<Event> events) throws ConverterException {
        if (events == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated events could not be " +
                    "received.");
        }

        List<EventDTO> eventDTOS = new LinkedList<>();

        for (Event event : events) {
            eventDTOS.add(convertToEventDTO(event));
        }

        return eventDTOS;
    }

    public List<EventBookingDTO> convertToEventBookingDTOList(final List<EventBooking> eventBookings) throws ConverterException {
        if (eventBookings == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated event bookings could not be " +
                    "received.");
        }

        List<EventBookingDTO> eventBookingDTOS = new LinkedList<>();

        for (EventBooking eventBooking : eventBookings) {
            eventBookingDTOS.add(convertToEventBookingDTO(eventBooking));
        }

        return eventBookingDTOS;
    }

    public EventBookingDTO convertToEventBookingDTO(final EventBooking eventBooking) throws ConverterException {
        if (eventBooking == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The generated event booking could not be received" +
                    ".");
        }

        return new EventBookingDTO(convertToEventDTO(eventBooking.getBookedEvent()), eventBooking.getTravellerName(),
                eventBooking.getBookingStatus(), eventBooking.getHotelBookingId());
    }

    public EventDTO convertToEventDTO(final Event event) throws ConverterException {
        if (event == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The included event information for the received " +
                    "booking is missing.");
        }

        return new EventDTO(event.getId(), event.getEventName(), event.getDate(),
                convertToAddressDTO(event.getAddress()), event.getMaxNumberOfPersons(), event.getPrice());
    }

    private AddressDTO convertToAddressDTO(final Address address) throws ConverterException {
        if (address == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The address for the event booking is missing.");
        }

        return new AddressDTO(address.getCountry(), address.getCity(), address.getStreet(), address.getStreetNumber());
    }
}
