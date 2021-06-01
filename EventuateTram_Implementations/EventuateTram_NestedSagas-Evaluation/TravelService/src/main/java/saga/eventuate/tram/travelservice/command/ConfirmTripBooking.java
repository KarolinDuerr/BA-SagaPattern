package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;

public class ConfirmTripBooking implements Command {

    private final long tripId;

    private final long hotelId;

    private final long flightId;

    private final long eventBookingId;

    private ConfirmTripBooking() {
        tripId = -1;
        hotelId = -1;
        flightId = -1;
        eventBookingId = -1;
    }

    public ConfirmTripBooking(final long tripId, final long hotelId, final long flightId, final long eventBookingId) {
        this.tripId = tripId;
        this.hotelId = hotelId;
        this.flightId = flightId;
        this.eventBookingId = eventBookingId;
    }

    public long getTripId() {
        return tripId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    @Override
    public String toString() {
        return "ConfirmTripBooking{" +
                "tripId=" + tripId +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
