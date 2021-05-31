package saga.netflix.conductor.hotelservice.api;

public class HotelServiceTasks {

    public static class Task {

        public static final String BOOK_HOTEL = "bookHotel";

        public static final String CANCEL_HOTEL_BOOKING = "cancelHotelBooking";

        public static final String CANCEL_HOTEL = "cancelHotel";

        public static final String CONFIRM_HOTEL = "confirmHotel";

        public static final String CONFIRM_HOTEL_CANCELLATION = "confirmHotelCancellation";

        public static final String REBOOK_HOTEL = "rebookHotel";
    }

    public static class TaskInput {

        public static final String BOOK_HOTEL_INPUT = "bookHotelRequest";

        public static final String CANCEL_HOTEL_BOOKING_INPUT = "cancelHotelBooking";

        public static final String CANCEL_HOTEL_INPUT = "cancelHotelRequest";

        public static final String CONFIRM_HOTEL_INPUT = "confirmHotelBooking";

        public static final String CONFIRM_HOTEL_CANCELLATION_INPUT = "confirmHotelCancellation";

        public static final String REBOOK_HOTEL_INPUT = "rebookHotel";

    }

    public static class TaskOutput {

        public static final String BOOK_HOTEL_OUTPUT = "bookHotelResponse";

        public static final String CANCEL_HOTEL_OUTPUT = "cancelHotelResponse";

    }
}
