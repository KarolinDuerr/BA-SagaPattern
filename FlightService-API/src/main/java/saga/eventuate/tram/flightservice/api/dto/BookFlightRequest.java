package saga.eventuate.tram.flightservice.api.dto;

import java.util.List;

public class BookFlightRequest {

    private FlightDTO outboundFlight;

    private FlightDTO returnFlight;

    private boolean oneWay;

    private List<String> travellerNames;

    public BookFlightRequest() {

    }

    public BookFlightRequest(final FlightDTO outboundFlight, final FlightDTO returnFlight, final boolean oneWay,
                             final List<String> travellerNames) {
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.oneWay = oneWay;
        this.travellerNames = travellerNames;
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

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public List<String> getTravellerNames() {
        return travellerNames;
    }

    public void setTravellerNames(final List<String> travellerNames) {
        this.travellerNames = travellerNames;
    }

    @Override
    public String toString() {
        return "BookFlightRequest{" +
                "outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", oneWay=" + oneWay +
                ", travellerNames=" + travellerNames +
                '}';
    }
}
