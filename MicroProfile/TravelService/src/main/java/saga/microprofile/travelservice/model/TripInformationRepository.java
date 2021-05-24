package saga.microprofile.travelservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class TripInformationRepository {

    @PersistenceContext(name = "TripInformationRepository")
    private EntityManager entityManager;

    public void save(final TripInformation tripInformation) {
        entityManager.persist(tripInformation);
    }

    public List<TripInformation> findAll() {
        return entityManager.createNamedQuery("TripInformation.findAll", TripInformation.class).getResultList();
    }

    public TripInformation findById(final Long tripId) {
        return entityManager.find(TripInformation.class, tripId);
    }

    public List<TripInformation> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("TripInformation.findTripByName", TripInformation.class)
                .setParameter("travellerName", travellerName).getResultList();
    }
}
