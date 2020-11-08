package saga.netflix.conductor.travelservice.api;

public class TravelServiceTasks {

    public static class Task {

        public static final String REJECT_TRIP = "rejectTrip";

    }

    public static class TaskInput {

        public static final String REJECT_TRIP_INPUT = "rejectTripBooking";

    }
}
