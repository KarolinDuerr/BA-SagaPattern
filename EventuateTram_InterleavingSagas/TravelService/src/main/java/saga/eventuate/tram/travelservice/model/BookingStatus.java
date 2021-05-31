package saga.eventuate.tram.travelservice.model;

public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLING, CANCELLED, CONFIRMED_HOTEL_CANCELLATION_REJECTED,
    CONFIRMED_FLIGHT_CANCELLATION_REJECTED, REJECTED_NO_FLIGHT_AVAILABLE, REJECTED_NO_HOTEL_AVAILABLE, REJECTED_UNKNOWN
}