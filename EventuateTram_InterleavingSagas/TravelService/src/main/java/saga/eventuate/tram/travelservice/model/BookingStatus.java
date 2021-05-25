package saga.eventuate.tram.travelservice.model;

public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLING, CANCELLED, REJECTED_NO_FLIGHT_AVAILABLE, REJECTED_NO_HOTEL_AVAILABLE, REJECTED_UNKNOWN
}
