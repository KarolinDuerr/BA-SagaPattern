package saga.eventuate.tram.flightservice.model.dto;

import saga.eventuate.tram.flightservice.api.dto.FlightDTO;
import saga.eventuate.tram.flightservice.model.BookingStatus;

import java.util.List;

public class FlightInformationDTO {

    private Long id;

    private FlightDTO outboundFlight;

    private FlightDTO returnFlight;

    private boolean oneWay;

    private List<String> travellerNames;

    private BookingStatus bookingStatus;

    private Long tripId;

    public FlightInformationDTO() {

    }

    public FlightInformationDTO(final long id, final FlightDTO outboundFlight, final FlightDTO returnFlight, final boolean oneWay,
                                final List<String> travellerNames, BookingStatus bookingStatus) {
        this.id = id;
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.oneWay = oneWay;
        this.travellerNames = travellerNames;
        this.bookingStatus = bookingStatus;
    }

    public FlightInformationDTO(final long id, final FlightDTO outboundFlight, final FlightDTO returnFlight, final boolean oneWay,
                                final List<String> travellerNames, BookingStatus bookingStatus, final long tripId) {
        this.id = id;
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.oneWay = oneWay;
        this.travellerNames = travellerNames;
        this.bookingStatus = bookingStatus;
        this.tripId = tripId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean getOneWay() {
        return oneWay;
    }

    public void setOneWay(final boolean oneWay) {
        this.oneWay = oneWay;
    }

    public List<String> getTravellerNames() {
        return travellerNames;
    }

    public void setTravellerNames(final List<String> travellerNames) {
        this.travellerNames = travellerNames;
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

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "FlightInformationDTO{" +
                "id=" + id +
                ", outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", oneWay=" + oneWay +
                ", travellerNames=" + travellerNames +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
