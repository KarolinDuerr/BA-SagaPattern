package saga.eventuate.tram.hotelservice.model;

import javax.persistence.Embeddable;

@Embeddable
public class Destination {

    private String country;

    private String city;

    public Destination() {

    }

    public Destination(final String country, final String city) {
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
        return "Destination{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
