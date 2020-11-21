package saga.eventuate.tram.hotelservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ConfirmHotelBooking implements Command {

    private final long tripId;

    private final long bookingId;

    private ConfirmHotelBooking(final long bookingId) {
        tripId = -1; // no trip assigned to the confirmed booking
        this.bookingId = bookingId;
    }

    public ConfirmHotelBooking(final long bookingId, final long tripId) {
        this.bookingId = bookingId;
        this.tripId = tripId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "ConfirmHotelBooking{" +
                "tripId=" + tripId +
                ", bookingId=" + bookingId +
                '}';
    }
}
