package saga.eventuate.tram.travelservice.api.dto;

import javax.validation.constraints.NotBlank;

public class LocationDTO {

    @NotBlank(message = "Country cannot be missing")
    private String country;

    @NotBlank(message = "City cannot be missing")
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
