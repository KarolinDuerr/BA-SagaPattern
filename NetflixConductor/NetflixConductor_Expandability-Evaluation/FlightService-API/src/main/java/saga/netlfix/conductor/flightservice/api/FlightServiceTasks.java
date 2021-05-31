package saga.netlfix.conductor.flightservice.api;

public class FlightServiceTasks {

    public static class Task {

        public static final String BOOK_FLIGHT = "bookFlight";

        public static final String CANCEL_FLIGHT = "cancelFlight";
    }

    public static class TaskInput {

        public static final String BOOK_FLIGHT_INPUT = "bookFlightRequest";

        public static final String CANCEL_FLIGHT_INPUT = "cancelFlightBooking";

    }

    public static class TaskOutput {

        public static final String BOOK_FLIGHT_OUTPUT = "bookFlightResponse";

    }
}
