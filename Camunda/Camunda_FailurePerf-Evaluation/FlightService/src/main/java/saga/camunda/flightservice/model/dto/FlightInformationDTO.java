package saga.camunda.flightservice.model.dto;

import saga.camunda.flightservice.model.BookingStatus;

public class FlightInformationDTO {

    private Long id;

    private FlightDTO outboundFlight;

    private FlightDTO returnFlight;

    private String travellerName;

    private BookingStatus bookingStatus;

    private Long tripId;

    private FlightInformationDTO() {

    }

    public FlightInformationDTO(final long id, final FlightDTO outboundFlight, final FlightDTO returnFlight,
                                final String travellerName, final BookingStatus bookingStatus) {
        this.id = id;
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerName = travellerName;
        this.bookingStatus = bookingStatus;
    }

    public FlightInformationDTO(final long id, final FlightDTO outboundFlight, final FlightDTO returnFlight,
                                final String travellerName, final BookingStatus bookingStatus, final long tripId) {
        this.id = id;
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerName = travellerName;
        this.bookingStatus = bookingStatus;
        this.tripId = tripId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public FlightDTO getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(final FlightDTO outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public FlightDTO getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(final FlightDTO returnFlight) {
        this.returnFlight = returnFlight;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(final String travellerName) {
        this.travellerName = travellerName;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(final Long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "FlightInformationDTO{" +
                "id=" + id +
                ", outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", travellerName=" + travellerName +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
