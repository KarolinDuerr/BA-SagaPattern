package saga.microprofile.travelservice.api.dto;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class LocationDTO implements Serializable {

    @NotBlank(message = "Country cannot be missing")
    private String country;

    @NotBlank(message = "City cannot be missing")
    private String city;

    public LocationDTO() {

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
