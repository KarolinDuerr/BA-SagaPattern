package saga.camunda.travelservice.api;

public class TravelServiceTopics {

    public static class DataInput {

        public static final String BOOK_TRIP_ID = "bookTripId";

    }

    public static class BpmnError {

        public static final String REJECT_TRIP_REASON = "errorMessage";

    }

    public static class Sagas {

        public static final String BOOK_TRIP_SAGA = "BookTripSaga";

    }
}
