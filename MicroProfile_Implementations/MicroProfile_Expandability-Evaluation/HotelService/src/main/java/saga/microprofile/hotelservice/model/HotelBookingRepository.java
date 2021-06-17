package saga.microprofile.hotelservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class HotelBookingRepository {

    @PersistenceContext(name = "HotelBookingRepository")
    private EntityManager entityManager;

    @Transactional
    public void save(final HotelBooking hotelBooking) {
        entityManager.persist(hotelBooking);
    }

    @Transactional
    public void update(final HotelBooking hotelBooking) {
        entityManager.merge(hotelBooking);
    }

    @Transactional
    public List<HotelBooking> findAll() {
        return entityManager.createNamedQuery("HotelBooking.findAll", HotelBooking.class).getResultList();
    }

    @Transactional
    public HotelBooking findById(final Long bookingId) {
        return entityManager.find(HotelBooking.class, bookingId);
    }

    @Transactional
    public List<HotelBooking> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("HotelBooking.findHotelByName", HotelBooking.class)
                .setParameter("travellerName", travellerName).getResultList();
    }

    @Transactional
    public List<HotelBooking> findByLraId(final String lraId) {
        return entityManager.createNamedQuery("HotelBooking.findByLraId", HotelBooking.class)
                .setParameter("lraId", lraId).getResultList();
    }
}
