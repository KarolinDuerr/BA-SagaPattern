package saga.camunda.flightservice.api;

public class FlightServiceTopics {

    public static class DataInput {

        public static final String BOOK_FLIGHT_DATA = "bookFlightRequest";

    }

    public static class DataOutput {

        public static final String BOOK_FLIGHT_RESPONSE = "bookFlightResponse";

    }

    public static class BpmnError {

        public static final String FLIGHT_ERROR = "FLIGHT_ERROR";

    }
}
