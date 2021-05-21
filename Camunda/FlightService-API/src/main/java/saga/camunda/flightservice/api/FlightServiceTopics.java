package saga.camunda.flightservice.api;

public class FlightServiceTopics {

    public static class DataInput {

        public static final String BOOK_FLIGHT_DATA = "bookFlightRequest";

    }

    public static class DataOutput {

        public static final String BOOK_FLIGHT_RESPONSE = "bookFlightResponse";

    }
}
