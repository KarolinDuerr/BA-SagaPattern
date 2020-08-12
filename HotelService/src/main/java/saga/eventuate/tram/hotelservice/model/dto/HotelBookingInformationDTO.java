package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.api.dto.DestinationDTO;
import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;

public class HotelBookingInformationDTO {

    private DestinationDTO destination;

    private StayDurationDTO duration;

    private int numberOfPersons;

    private int numberOfRooms;

    public HotelBookingInformationDTO() {

    }

    public HotelBookingInformationDTO(final DestinationDTO destination, final StayDurationDTO duration,
                                      final int numberOfPersons, final int numberOfRooms) {
        this.destination = destination;
        this.duration = duration;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
    }

    public void setDestination(final DestinationDTO destination) {
        this.destination = destination;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDuration(final StayDurationDTO duration) {
        this.duration = duration;
    }

    public StayDurationDTO getDuration() {
        return duration;
    }

    public void setNumberOfPersons(final int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfRooms(final int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    @Override
    public String toString() {
        return "HotelBookingInformationDTO{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                '}';
    }
}
