package saga.netlfix.conductor.flightservice.api;

public class FlightServiceTasks {

    public static class Task {

        public static final String BOOK_FLIGHT = "bookFlight";

        public static final String CANCEL_FLIGHT_BOOKING = "cancelFlightBooking";

        public static final String CANCEL_FLIGHT = "cancelFlight";

        public static final String REBOOK_FLIGHT = "rebookFlight";
    }

    public static class TaskInput {

        public static final String BOOK_FLIGHT_INPUT = "bookFlightRequest";

        public static final String CANCEL_FLIGHT_BOOKING_INPUT = "cancelFlightBooking";

        public static final String CANCEL_FLIGHT_INPUT = "cancelFlightRequest";

    }

    public static class TaskOutput {

        public static final String BOOK_FLIGHT_OUTPUT = "bookFlightResponse";

        public static final String CANCEL_FLIGHT_OUTPUT = "cancelFlightResponse";

    }
}
