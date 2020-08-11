package saga.eventuate.tram.flightservice.api.dto;

public class BookFlightRequest {

    private FlightDTO outboundFlight;

    private FlightDTO returnFlight;

    private boolean oneWay;

    private String travellerName;

    public BookFlightRequest() {

    }

    public BookFlightRequest(final FlightDTO outboundFlight, final FlightDTO returnFlight, final boolean oneWay,
                             final String travellerName) {
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.oneWay = oneWay;
        this.travellerName = travellerName;
    }

    public FlightDTO getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(FlightDTO outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public FlightDTO getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(FlightDTO returnFlight) {
        this.returnFlight = returnFlight;
    }

    public boolean getOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public String getTravellerName() {
        return travellerName;
    }

    public void setTravellerName(String travellerName) {
        this.travellerName = travellerName;
    }

    @Override
    public String toString() {
        return "BookFlightRequest{" +
                "outboundFlight=" + outboundFlight +
                ", returnFlight=" + returnFlight +
                ", oneWay=" + oneWay +
                ", travellerName='" + travellerName + '\'' +
                '}';
    }
}
