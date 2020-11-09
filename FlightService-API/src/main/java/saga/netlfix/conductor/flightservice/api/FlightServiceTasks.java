package saga.netlfix.conductor.flightservice.api;

public class FlightServiceTasks {

    public static class Task {

        public static final String BOOK_FLIGHT = "bookFlight";

        public static final String CANCEL_FLIGHT = "cancelFlight";
    }

    public static class TaskInput {

        public static final String BOOK_FLIGHT_INPUT = "bookFlightTask";

        public static final String CANCEL_FLIGHT_INPUT = "cancelFlightTask";

    }

    public static class TaskOutput {

        public static final String BOOK_FLIGHT_OUTPUT = "bookFlightResponse";

    }
}
