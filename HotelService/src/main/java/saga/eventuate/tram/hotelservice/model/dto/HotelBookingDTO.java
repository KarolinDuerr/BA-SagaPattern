package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.model.BookingStatus;

public class HotelBookingDTO {

    private Long id;

    private String hotelName;

    private HotelBookingInformationDTO bookingInformation;

    private BookingStatus bookingStatus;

    private int tripId;

    public HotelBookingDTO() {

    }

    public HotelBookingDTO(final String hotelName, final HotelBookingInformationDTO bookingInformation) {
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.tripId = -1; // no trip assigned to this booking
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public HotelBookingDTO(final String hotelName, final HotelBookingInformationDTO bookingInformation, final int tripId) {
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.tripId = tripId;
        this.bookingStatus = BookingStatus.CONFIRMED;
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

    public HotelBookingInformationDTO getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(HotelBookingInformationDTO bookingInformation) {
        this.bookingInformation = bookingInformation;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                ", tripId=" + tripId +
                '}';
    }
}
