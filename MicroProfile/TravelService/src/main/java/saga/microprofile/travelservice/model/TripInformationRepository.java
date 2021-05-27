package saga.microprofile.travelservice.model;

import saga.microprofile.travelservice.error.TravelException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RequestScoped
//@ApplicationScoped
public class TripInformationRepository {

    @PersistenceContext(name = "TripInformationRepository")
    private EntityManager entityManager;

    public void save(final TripInformation tripInformation) {
        entityManager.persist(tripInformation);
    }

    public List<TripInformation> findAll() {
//        return entityManager.createNamedQuery("TripInformation.findAll", TripInformation.class).getResultList();
        List<TripInformation> test = new LinkedList<>();
        try {
            TripDuration tripDuration = new TripDuration(LocalDate.now(), LocalDate.now().plusDays(2));
            Location location = new Location("test1", "test1");
            Location locationEnd = new Location("destination", "destination");
            TripInformation t1 = new TripInformation(tripDuration, location, locationEnd,"test", "test", 1);
            TripInformation t2 = new TripInformation(tripDuration, locationEnd, location,"test2", "test2", 2);
            t1.setFlightId(1);
            t2.setFlightId(1);
            t1.setHotelId(2);
            t2.setHotelId(2);
            t1.setId(1L);
            t2.setId(2L);
            test.add(t1);
            test.add(t2);
        } catch (TravelException e) {
            e.printStackTrace();
        }
        return test;
    }

    public TripInformation findById(final Long tripId) {
        return entityManager.find(TripInformation.class, tripId);
    }

    public List<TripInformation> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("TripInformation.findTripByName", TripInformation.class)
                .setParameter("travellerName", travellerName).getResultList();
    }
}
