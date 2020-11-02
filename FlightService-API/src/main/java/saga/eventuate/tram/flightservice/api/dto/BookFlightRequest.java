package saga.eventuate.tram.flightservice.api.dto;

import java.util.List;

public class BookFlightRequest {

    private FlightDTO outboundFlight;

    private FlightDTO returnFlight;

    private String travellerName;

    private BookFlightRequest() {

    }

    public BookFlightRequest(final FlightDTO outboundFlight, final FlightDTO returnFlight, final boolean oneWay,
                             final String travellerName) {
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.travellerName = travellerName;
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

    @Override
    public String toString() {
        return "BookFlightRequest{" +
                "outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", travellerName=" + travellerName +
                '}';
    }
}
