package saga.eventuate.tram.travelservice.saga;

public class BookTripSagaData {

    private final long tripId;

    public BookTripSagaData() {
        this.tripId = -1;
    }

    public BookTripSagaData(final long tripId) {
        this.tripId = tripId;
    }

    public long getTripId() {
        return tripId;
    }
}
