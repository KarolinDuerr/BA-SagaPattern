package saga.microprofile.travelservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
//@ApplicationScoped
public class TripInformationRepository {

    @PersistenceContext(name = "TripInformationRepository")
    private EntityManager entityManager;

    @Transactional
    public void save(final TripInformation tripInformation) {
        entityManager.persist(tripInformation);
    }

    @Transactional
    public void update(final TripInformation tripInformation) {
        entityManager.merge(tripInformation);
    }

    @Transactional
    public List<TripInformation> findAll() {
        return entityManager.createNamedQuery("TripInformation.findAll", TripInformation.class).getResultList();
    }

    @Transactional
    public TripInformation findById(final Long id) {
        return entityManager.find(TripInformation.class, id);
    }

    @Transactional
    public List<TripInformation> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("TripInformation.findTripByName", TripInformation.class)
                .setParameter("travellerName", travellerName).getResultList();
    }

    @Transactional
    public List<TripInformation> findByLraId(final String lraId) {
        return entityManager.createNamedQuery("TripInformation.findByLraId", TripInformation.class)
                .setParameter("lraId", lraId).getResultList();
    }
}
