package saga.camunda.flightservice.api.dto;

import java.io.Serializable;

public class LocationDTO implements Serializable {

    private String country;

    private String city;

    private LocationDTO() {

    }

    public LocationDTO(final String country, final String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
