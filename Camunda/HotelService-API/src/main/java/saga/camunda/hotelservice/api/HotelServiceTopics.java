package saga.camunda.hotelservice.api;

public class HotelServiceTopics {

    public static class DataInput {

        public static final String BOOK_HOTEL_DATA = "bookHotelRequest";

    }

    public static class DataOutput {

        public static final String BOOK_HOTEL_RESPONSE = "bookHotelResponse";

    }

    public static class BpmnError {

        public static final String HOTEL_ERROR = "HOTEL_ERROR";

    }
}
