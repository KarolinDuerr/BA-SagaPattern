package saga.eventuate.tram.flightservice.model;

import org.springframework.data.repository.CrudRepository;

public interface FlightInformationRepository extends CrudRepository<FlightInformation, Long> {
}
