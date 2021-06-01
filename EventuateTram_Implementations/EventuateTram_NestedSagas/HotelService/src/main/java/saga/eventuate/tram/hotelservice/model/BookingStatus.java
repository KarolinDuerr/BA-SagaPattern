package saga.eventuate.tram.hotelservice.model;

public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED, REJECTED_NO_HOTEL_EVENT_AVAILABLE, REJECTED_UNKNOWN
}
