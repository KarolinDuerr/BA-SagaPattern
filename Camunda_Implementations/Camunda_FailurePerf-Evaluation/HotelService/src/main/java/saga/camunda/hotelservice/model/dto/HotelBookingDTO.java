package saga.camunda.hotelservice.model.dto;


import saga.camunda.hotelservice.model.BookingStatus;

public class HotelBookingDTO {

    private Long id;

    private String hotelName;

    private String travellerName;

    private HotelBookingInformationDTO bookingInformation;

    private BookingStatus bookingStatus;

    private HotelBookingDTO() {

    }

    public HotelBookingDTO(final long id, final String hotelName, final String travellerName,
                           final HotelBookingInformationDTO bookingInformation, final BookingStatus bookingStatus) {
        this.id = id;
        this.hotelName = hotelName;
        this.travellerName = travellerName;
        this.bookingInformation = bookingInformation;
        this.bookingStatus = bookingStatus;
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

    @Override
    public String toString() {
        return "HotelBookingDTO{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", travellerName='" + travellerName + '\'' +
                ", bookingInformation=" + bookingInformation +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
