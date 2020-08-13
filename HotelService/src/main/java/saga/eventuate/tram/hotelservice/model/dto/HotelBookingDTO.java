package saga.eventuate.tram.hotelservice.model.dto;

import saga.eventuate.tram.hotelservice.model.BookingStatus;

public class HotelBookingDTO {

    private Long id;

    private String hotelName;

    private HotelBookingInformationDTO bookingInformation;

    private BookingStatus bookingStatus;

    public HotelBookingDTO() {

    }

    public HotelBookingDTO(final long id, final String hotelName, final HotelBookingInformationDTO bookingInformation) {
        this.id = id;
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public HotelBookingDTO(final long id, final String hotelName, final HotelBookingInformationDTO bookingInformation
            , final BookingStatus bookingStatus) {
        this.id = id;
        this.hotelName = hotelName;
        this.bookingInformation = bookingInformation;
        this.bookingStatus = bookingStatus;
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

    @Override
    public String toString() {
        return "HotelBooking{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
