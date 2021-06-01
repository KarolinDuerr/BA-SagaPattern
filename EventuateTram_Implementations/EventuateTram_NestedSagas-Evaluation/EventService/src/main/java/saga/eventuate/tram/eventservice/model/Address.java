package saga.eventuate.tram.eventservice.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String country;

    private String city;

    private String street;

    private int streetNumber;

    private Address() {

    }

    public Address(final String country, final String city, final String street, final int streetNumber) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber=" + streetNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Address)) {
            return false;
        }

        Address address = (Address) o;

        if (address.getCountry() == null || !address.getCountry().equalsIgnoreCase(this.getCountry())) {
            return false;
        }

        if (address.getCity() == null || !address.getCity().equalsIgnoreCase(this.getCity())) {
            return false;
        }

        if (address.getStreet() == null || !address.getStreet().equalsIgnoreCase(this.getStreet())) {
            return false;
        }

        return address.getStreetNumber() == this.getStreetNumber();
    }
}