package saga.microprofile.flightservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class FlightInformationRepository {

    @PersistenceContext(name = "HotelBookingRepository")
    private EntityManager entityManager;

    public void save(final FlightInformation flightBooking) {
        entityManager.persist(flightBooking);
    }

    public void update(final FlightInformation flightBooking) {
        entityManager.merge(flightBooking);
    }

    public List<FlightInformation> findAll() {
        return entityManager.createNamedQuery("FlightBooking.findAll", FlightInformation.class).getResultList();
    }

    public FlightInformation findById(final Long bookingId) {
        return entityManager.find(FlightInformation.class, bookingId);
    }

    public List<FlightInformation> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("FlightBooking.findFlightByName", FlightInformation.class)
                .setParameter("travellerName", travellerName).getResultList();
    }
}
