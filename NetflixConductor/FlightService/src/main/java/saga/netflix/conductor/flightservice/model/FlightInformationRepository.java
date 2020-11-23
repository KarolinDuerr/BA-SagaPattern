package saga.netflix.conductor.flightservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightInformationRepository extends CrudRepository<FlightInformation, Long> {

    List<FlightInformation> findByTravellerName(String travellerName);
}
