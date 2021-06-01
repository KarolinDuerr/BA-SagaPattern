package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelBooking implements Command {

    private final long tripId;

    private final long bookingId;

    private final long eventBookingId; // TODO check if necessary

    // default constructor necessary for Eventuate Framework
    private ConfirmHotelBooking() {
        tripId = -1;
        bookingId = -1;
        eventBookingId = -1;
    }

    public ConfirmHotelBooking(final long bookingId, final long tripId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
        this.eventBookingId = -1;
    }

    public ConfirmHotelBooking(final long bookingId, final long tripId, final long eventBookingId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
        this.eventBookingId = eventBookingId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    @Override
    public String toString() {
        return "ConfirmHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
