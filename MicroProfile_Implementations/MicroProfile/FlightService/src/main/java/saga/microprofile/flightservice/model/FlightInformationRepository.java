package saga.microprofile.flightservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class FlightInformationRepository {

    @PersistenceContext(name = "HotelBookingRepository")
    private EntityManager entityManager;

    @Transactional
    public void save(final FlightInformation flightBooking) {
        entityManager.persist(flightBooking);
    }

    @Transactional
    public void update(final FlightInformation flightBooking) {
        entityManager.merge(flightBooking);
    }

    @Transactional
    public List<FlightInformation> findAll() {
        return entityManager.createNamedQuery("FlightBooking.findAll", FlightInformation.class).getResultList();
    }

    @Transactional
    public FlightInformation findById(final Long bookingId) {
        return entityManager.find(FlightInformation.class, bookingId);
    }

    @Transactional
    public List<FlightInformation> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("FlightBooking.findFlightByName", FlightInformation.class)
                .setParameter("travellerName", travellerName).getResultList();
    }

    @Transactional
    public List<FlightInformation> findByLraId(final String lraId) {
        return entityManager.createNamedQuery("FlightBooking.findByLraId", FlightInformation.class)
                .setParameter("lraId", lraId).getResultList();
    }
}
