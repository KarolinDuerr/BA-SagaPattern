package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.model.BookingStatus;

public class HotelBookingDTO {

    private Long id;

    private String hotelName;

    private HotelBookingInformationDTO bookingInformation;

    private BookingStatus bookingStatus;

    private final long eventBookingId;

    private HotelBookingDTO() {
        eventBookingId = -1; // no event assigned to this booking
    }

    public HotelBookingDTO(final long id, final String hotelName, final HotelBookingInformationDTO bookingInformation,
                           final BookingStatus bookingStatus, final long eventBookingId) {
        this.id = id;
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.bookingStatus = bookingStatus;
        this.eventBookingId = eventBookingId;
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

    public HotelBookingInformationDTO getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(final HotelBookingInformationDTO bookingInformation) {
        this.bookingInformation = bookingInformation;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    @Override
    public String toString() {
        return "HotelBookingDTO{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
