package saga.netflix.conductor.travelservice.api;

public class TravelServiceTasks {

    public static class Task {

        public static final String CONFIRM_TRIP = "confirmTrip";

        public static final String CANCEL_TRIP = "cancelTrip";

        public static final String REJECT_TRIP = "rejectTrip";

    }

    public static class TaskInput {

        public static final String CONFIRM_TRIP_HOTEL_INPUT = "confirmHotelInTripBooking";

        public static final String CONFIRM_TRIP_FLIGHT_INPUT = "confirmFlightInTripBooking";

        public static final String REJECT_TRIP_INPUT = "rejectTripBooking";

        public static final String COMPENSATING_TRIP_BOOKING = "cancelTripBooking";
    }
}
