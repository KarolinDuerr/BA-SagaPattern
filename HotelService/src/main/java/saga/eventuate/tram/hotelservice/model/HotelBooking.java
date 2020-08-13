package saga.eventuate.tram.hotelservice.model;

import saga.eventuate.tram.hotelservice.error.UnsupportedStateTransition;

import javax.persistence.*;

@Entity
@Table(name = "hotelBookings")
@Access(AccessType.FIELD)
public class HotelBooking {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private String hotelName;

    @Embedded
    private HotelBookingInformation bookingInformation;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private int tripId;

    public HotelBooking() {

    }

    public HotelBooking(final String hotelName, final HotelBookingInformation bookingInformation) {
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.tripId = -1; // no trip assigned to this booking
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public HotelBooking(final String hotelName, final HotelBookingInformation bookingInformation, final int tripId) {
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.tripId = tripId;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public HotelBookingInformation getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(HotelBookingInformation bookingInformation) {
        this.bookingInformation = bookingInformation;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void cancel(BookingStatus bookingStatus) {
        switch (bookingStatus) {
            case PENDING:
                this.bookingStatus = bookingStatus;
            default:
                throw new UnsupportedStateTransition("The hotel booking can only be rejected if its still PENDING, " +
                        "but the current status is: " + getBookingStatus());
        }
    }

    public void confirm(BookingStatus bookingStatus) {
        switch (bookingStatus) {
            case PENDING:
                this.bookingStatus = bookingStatus;
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
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                ", tripId=" + tripId +
                '}';
    }
}
