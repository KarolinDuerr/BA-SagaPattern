package saga.eventuate.tram.hotelservice.model;

import javax.persistence.*;

@Embeddable
public class HotelBookingInformation {

    @Embedded
    private Destination destination;

    @Embedded
    private StayDuration duration;

    private int numberOfPersons;

    private int numberOfRooms;

    public HotelBookingInformation() {

    }

    public HotelBookingInformation(final Destination destination, final StayDuration duration,
                                   final int numberOfPersons, final int numberOfRooms) {
        this.destination = destination;
        this.duration = duration;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
    }

    public void setDestination(final Destination destination) {
        this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDuration(final StayDuration duration) {
        this.duration = duration;
    }

    public StayDuration getDuration() {
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
        return "HotelBookingInformation{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                '}';
    }
}
