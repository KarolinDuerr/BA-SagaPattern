package saga.netflix.conductor.hotelservice.model;

import saga.netflix.conductor.hotelservice.error.UnsupportedStateTransition;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "hotelBookings")
@Access(AccessType.FIELD)
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private String hotelName;

    private String travellerName;

    @Embedded
    private HotelBookingInformation bookingInformation;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private HotelBooking() {

    }

    public HotelBooking(final String hotelName, final String travellerName, final HotelBookingInformation bookingInformation) {
        this.hotelName = hotelName;
        this.travellerName = travellerName;
        this.bookingInformation = bookingInformation;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(final String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public HotelBookingInformation getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(final HotelBookingInformation bookingInformation) {
        this.bookingInformation = bookingInformation;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void cancel(final BookingStatus bookingStatus) {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
            case CANCELLED:
                this.bookingStatus = bookingStatus;
                break;
            default:
                throw new UnsupportedStateTransition("The hotel booking can only be rejected if its still PENDING, " +
                        "but the current status is: " + getBookingStatus());
        }
    }

    public void confirm() {
        switch (this.bookingStatus) {
            case PENDING:
            case CONFIRMED:
                this.bookingStatus = BookingStatus.CONFIRMED;
                break;
            default:
                throw new UnsupportedStateTransition("The hotel booking  can only be confirmed if its still PENDING, " +
                        "but the current status is: " + getBookingStatus());
        }
    }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "id=" + id +
                ", version=" + version +
                ", hotelName='" + hotelName + '\'' +
                ", travellerName='" + travellerName + '\'' +
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof HotelBooking)) {
            return false;
        }

        HotelBooking hotelBooking = (HotelBooking) o;

        if (Objects.equals(hotelBooking.getId(), this.getId())) {
            return true;
        }

        if (!Objects.equals(hotelBooking.getBookingInformation(), this.getBookingInformation())) {
            return false;
        }

        return hotelBooking.getHotelName() != null && hotelBooking.getHotelName().equalsIgnoreCase(this.getHotelName());
    }
}
