package saga.netflix.conductor.hotelservice.api;

public class HotelServiceTasks {

    public static class Task {

        public static final String BOOK_HOTEL = "bookHotel";

        public static final String CANCEL_HOTEL = "cancelHotel";

        public static final String CONFIRM_HOTEL = "confirmHotel";
    }

    public static class TaskInput {

        public static final String BOOK_HOTEL_INPUT = "bookHotelRequest";

        public static final String CANCEL_HOTEL_INPUT = "cancelHotelBooking";

        public static final String CONFIRM_HOTEL_INPUT = "confirmHotelBooking";

    }

    public static class TaskOutput {

        public static final String BOOK_HOTEL_OUTPUT = "bookHotelResponse";

    }
}
