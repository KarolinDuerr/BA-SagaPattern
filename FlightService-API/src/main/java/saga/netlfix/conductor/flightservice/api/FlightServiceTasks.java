package saga.netlfix.conductor.flightservice.api;

public class FlightServiceTasks {

    public static class Task {

        public static final String BOOK_FLIGHT = "bookFlight";
    }

    public static class TaskInput {

        public static final String BOOK_FLIGHT_INPUT = "bookFlightTask";

    }

    public static class TaskOutput {

        public static final String BOOK_FLIGHT_OUTPUT = "bookFlightResponse";

    }
}
