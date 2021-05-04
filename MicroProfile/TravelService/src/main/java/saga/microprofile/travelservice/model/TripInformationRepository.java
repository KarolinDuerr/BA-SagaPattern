package saga.microprofile.travelservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripInformationRepository extends CrudRepository<TripInformation, Long> {
    
    List<TripInformation> findByTravellerName(String travellerName);
}
