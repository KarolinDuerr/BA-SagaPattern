package saga.eventuate.tram.hotelservice.model;

import org.springframework.data.repository.CrudRepository;

public interface HotelBookingRepository extends CrudRepository<HotelBooking, Long> {
}
