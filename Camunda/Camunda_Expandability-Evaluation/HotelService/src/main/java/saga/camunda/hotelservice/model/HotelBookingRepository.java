package saga.camunda.hotelservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelBookingRepository extends CrudRepository<HotelBooking, Long> {
    List<HotelBooking> findByTravellerName(String travellerName);
}
