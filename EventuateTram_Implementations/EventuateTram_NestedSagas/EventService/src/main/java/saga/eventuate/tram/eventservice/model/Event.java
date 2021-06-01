package saga.eventuate.tram.eventservice.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "hotelEvents")
@Access(AccessType.FIELD)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private String eventName;

    private Date date;

    @Embedded
    private Address address;

    private Long maxNumberOfPersons;

    private double price;

    public Event() {

    }

    public Event(final String eventName, final Date date, final Address address, final Long maxNumberOfPersons,
                 final double price) {
        this.eventName = eventName;
        this.date = date;
        this.address = address;
        this.maxNumberOfPersons = maxNumberOfPersons;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Long getMaxNumberOfPersons() {
        return maxNumberOfPersons;
    }

    public void setMaxNumberOfPersons(final Long maxNumberOfPersons) {
        this.maxNumberOfPersons = maxNumberOfPersons;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", version=" + version +
                ", eventName='" + eventName + '\'' +
                ", date=" + date +
                ", address=" + address +
                ", maxNumberOfPersons=" + maxNumberOfPersons +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Event)) {
            return false;
        }

        Event event = (Event) o;

        if (Objects.equals(event.getId(), this.getId())) {
            return true;
        }

        if (event.getEventName() == null || !event.getEventName().equalsIgnoreCase(this.getEventName())) {
            return false;
        }

        if (event.getDate().compareTo(this.getDate()) != 0 && event.getDate().getTime() != this.getDate().getTime()) {
            return false;
        }

        if (!Objects.equals(event.getAddress(), this.getAddress())) {
            return false;
        }

        if (!Objects.equals(event.getMaxNumberOfPersons(), this.getMaxNumberOfPersons())) {
            return false;
        }

        return event.getPrice() == this.getPrice();
    }
}
