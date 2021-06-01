package saga.eventuate.tram.eventservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventBookingRepository extends CrudRepository<EventBooking, Long> {

    List<EventBooking> findByTravellerName(String travellerName);
}
