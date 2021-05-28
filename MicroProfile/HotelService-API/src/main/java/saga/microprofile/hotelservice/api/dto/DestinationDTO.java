package saga.microprofile.hotelservice.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;

public class DestinationDTO {

    private String country;

    private String city;

    private DestinationDTO() {

    }

    public DestinationDTO(final String country, final String city) {
        this.country = country;
        this.city = city;
    }

    @JsonbProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @JsonbProperty("city")
    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "DestinationDTO{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
