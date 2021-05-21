package saga.camunda.hotelservice.api;

public class HotelServiceTopics {

    public static class DataInput {

        public static final String BOOK_HOTEL_DATA = "bookHotelData";

//        public static final String CONFIRM_HOTEL_DATA = "bookHotelResponse";

    }

    public static class DataOutput {

        public static final String BOOK_HOTEL_RESPONSE = "bookHotelResponse";

    }
}
