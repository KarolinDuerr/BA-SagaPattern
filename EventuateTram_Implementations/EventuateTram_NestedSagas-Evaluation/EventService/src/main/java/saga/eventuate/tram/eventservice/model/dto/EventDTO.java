package saga.eventuate.tram.eventservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class EventDTO {

    private Long id;

    private String eventName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;

    private AddressDTO address;

    private Long maxNumberOfPersons;

    private double price;

    private EventDTO() {

    }

    public EventDTO(final long id, final String eventName, final Date date, final AddressDTO address, final Long maxNumberOfPersons,
                    final double price) {
        this.id = id;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(final AddressDTO address) {
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
                ", eventName='" + eventName + '\'' +
                ", date=" + date +
                ", address=" + address +
                ", maxNumberOfPersons=" + maxNumberOfPersons +
                ", price=" + price +
                '}';
    }
}
