package saga.netflix.conductor.travelservice.model;

import javax.persistence.Embeddable;

@Embeddable
public class Location {

    private String country;

    private String city;

    private Location() {

    }

    public Location(final String country, final String city) {
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
        return "Location{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Location)) {
            return false;
        }

        Location location = (Location) o;

        if (!location.getCountry().equalsIgnoreCase(this.getCountry())) {
            return false;
        }

        return location.getCity().equalsIgnoreCase(this.getCity());
    }
}
