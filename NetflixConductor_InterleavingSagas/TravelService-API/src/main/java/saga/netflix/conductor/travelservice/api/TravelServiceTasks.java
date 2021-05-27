package saga.netflix.conductor.travelservice.api;

public class TravelServiceTasks {

    public static class Task {

        public static final String CONFIRM_TRIP = "confirmTrip";

        public static final String REJECT_TRIP = "rejectTrip";

        public static final String CONFIRM_TRIP_CANCELLATION = "confirmTripCancellation";

        public static final String REJECT_TRIP_CANCELLATION = "rejectTripCancellation";

    }

    public static class TaskInput {

        public static final String CONFIRM_TRIP_HOTEL_INPUT = "confirmHotelInTripBooking";

        public static final String CONFIRM_TRIP_FLIGHT_INPUT = "confirmFlightInTripBooking";

        public static final String REJECT_TRIP_INPUT = "reason";

        public static final String REJECT_TRIP_ID_INPUT = "tripIdInHotelBookingRequest";

        public static final String CONFIRM_TRIP_CANCELLATION_INPUT = "confirmTripCancellation";

        // TODO
        public static final String CANCEL_TRIP_ID = "cancelTripId";
    }
}
