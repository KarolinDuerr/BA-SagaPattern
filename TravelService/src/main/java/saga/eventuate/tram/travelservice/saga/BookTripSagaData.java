package saga.eventuate.tram.travelservice.saga;

import saga.eventuate.tram.flightservice.api.dto.BookFlightCommand;
import saga.eventuate.tram.flightservice.api.dto.LocationDTO;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripInformation;

public class BookTripSagaData {

    private final long tripId;

    private TripInformation tripInformation;

    private long hotelId;

    private long flightId;

    private RejectionReason rejectionReason;

    private BookTripSagaData() {
        this.tripId = 0;
        this.hotelId = -1;
        this.flightId = -1;
    }

    public BookTripSagaData(final long tripId, final TripInformation tripInformation) {
        this.tripId = tripId;
        this.tripInformation = tripInformation;
        this.hotelId = -1;
        this.flightId = -1;
    }

    public long getTripId() {
        return tripId;
    }

    public TripInformation getTripInformation() {
        return tripInformation;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(final long hotelId) {
        this.hotelId = hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(final long flightId) {
        this.flightId = flightId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public BookHotelRequest makeBookHotelRequest() {
        DestinationDTO destination = new DestinationDTO(tripInformation.getDestination().getCountry(),
                tripInformation.getDestination().getCity());
        StayDurationDTO stayDuration = new StayDurationDTO(tripInformation.getDuration().getStart(),
                tripInformation.getDuration().getEnd());
        return new BookHotelRequest(getTripId(), destination, stayDuration, tripInformation.getNumberOfPersons(),
                tripInformation.getNumberOfRooms());
    }

    public BookFlightCommand makeBookFlightCommand() {
        LocationDTO home = new LocationDTO(tripInformation.getStart().getCountry(),
                tripInformation.getStart().getCity());
        LocationDTO destination = new LocationDTO(tripInformation.getDestination().getCountry(),
                tripInformation.getDestination().getCity());
        return new BookFlightCommand(getTripId(), home, destination, tripInformation.getDuration().getStart(),
                tripInformation.getDuration().getEnd(),
                tripInformation.getOneWayFlight(), tripInformation.getTravellerNames());
    }

    @Override
    public String toString() {
        return "BookTripSagaData{" +
                "tripId=" + tripId +
                ", tripInformation=" + tripInformation +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
