package saga.microprofile.travelservice.model;

import saga.microprofile.travelservice.controller.TravelService;
import saga.microprofile.travelservice.error.TravelException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
//@ApplicationScoped
public class TripInformationRepository {

    private static final Logger logger = Logger.getLogger(TripInformationRepository.class.toString());

    @PersistenceContext(name = "TripInformationRepository")
    private EntityManager entityManager;

    public void save(final TripInformation tripInformation) {
        entityManager.persist(tripInformation);
    }

    public void update(final TripInformation tripInformation) {
        entityManager.merge(tripInformation);
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
