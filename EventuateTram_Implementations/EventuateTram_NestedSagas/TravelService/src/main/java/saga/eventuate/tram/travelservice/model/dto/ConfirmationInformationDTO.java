package saga.eventuate.tram.travelservice.model.dto;

import saga.eventuate.tram.travelservice.model.BookingStatus;

public class ConfirmationInformationDTO {

    private BookingStatus bookingStatus;

    private long hotelId = -1;

    private long flightId = -1;

    private long eventBookingId = -1;

    private ConfirmationInformationDTO() {

    }

    public ConfirmationInformationDTO(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public ConfirmationInformationDTO(final BookingStatus bookingStatus, final long hotelId, final long flightId,
                                      final long eventBookingId) {
        this.bookingStatus = bookingStatus;
        this.hotelId = hotelId;
        this.flightId = flightId;
        this.eventBookingId = eventBookingId;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(final long hotelId) {
        this.hotelId = hotelId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(final long flightId) {
        this.flightId = flightId;
    }

    public long getEventBookingId() {
        return eventBookingId;
    }

    public void setEventBookingId(long eventBookingId) {
        this.eventBookingId = eventBookingId;
    }

    @Override
    public String toString() {
        return "ConfirmationInformationDTO{" +
                "bookingStatus=" + bookingStatus +
                ", hotelId=" + hotelId +
                ", flightId=" + flightId +
                ", eventBookingId=" + eventBookingId +
                '}';
    }
}
