package saga.eventuate.tram.travelservice.command;

import io.eventuate.tram.commands.common.Command;

public class ConfirmTripCancellationCommand implements Command {

    private final long tripId;

    private final long hotelId;

    private final long flightId;

    private ConfirmTripCancellationCommand() {
        tripId = -1;
        hotelId = -1;
        flightId = -1;
    }

    public ConfirmTripCancellationCommand(final long tripId, final long hotelId, final long flightId) {
        this.tripId = tripId;
        this.hotelId = hotelId;
        this.flightId = flightId;
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

    @Override
    public String toString() {
        return "ConfirmTripCancellationBooking{" +
                "tripId=" + tripId +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                '}';
    }
}
