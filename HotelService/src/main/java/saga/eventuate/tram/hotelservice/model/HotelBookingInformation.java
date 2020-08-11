package saga.eventuate.tram.hotelservice.model;

import javax.persistence.*;

@Embeddable
public class HotelBookingInformation {

    private String country;

    private String city;

    @Embedded
    private StayDuration duration;

    private int numberOfPersons;

    private int numberOfRooms;

    public HotelBookingInformation() {

    }

    public HotelBookingInformation(final String country, final String city, final StayDuration duration,
                                   final int numberOfPersons, final int numberOfRooms) {
        this.country = country;
        this.city = city;
        this.duration = duration;
        this.numberOfPersons = numberOfPersons;
        this.numberOfRooms = numberOfRooms;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
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
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", duration=" + duration +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                '}';
    }
}
