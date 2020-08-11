package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.api.dto.StayDurationDTO;

public class HotelBookingInformationDTO {

    private String country;

    private String city;

    private StayDurationDTO duration;

    private int numberOfPersons;

    private int numberOfRooms;

    public HotelBookingInformationDTO() {

    }

    public HotelBookingInformationDTO(final String country, final String city, final StayDurationDTO duration,
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
        return "HotelBookingInformation{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", duration=" + duration +
                ", numberOfPersons=" + numberOfPersons +
                ", numberOfRooms=" + numberOfRooms +
                '}';
    }
}
