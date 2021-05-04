package saga.microprofile.hotelservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationDTO {

    private String country;

    private String city;

    private DestinationDTO() {

    }

    public DestinationDTO(final String country, final String city) {
        this.country = country;
        this.city = city;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @JsonProperty("city")
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
