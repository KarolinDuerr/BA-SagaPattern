package saga.microprofile.hotelservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class HotelBookingRepository {

    @PersistenceContext(name = "HotelBookingRepository")
    private EntityManager entityManager;

    public void save(final HotelBooking hotelBooking) {
        entityManager.persist(hotelBooking);
    }

    public void update(final HotelBooking hotelBooking) {
        entityManager.merge(hotelBooking);
    }

    public List<HotelBooking> findAll() {
        return entityManager.createNamedQuery("HotelBooking.findAll", HotelBooking.class).getResultList();
    }

    public HotelBooking findById(final Long bookingId) {
        return entityManager.find(HotelBooking.class, bookingId);
    }

    public List<HotelBooking> findByTravellerName(final String travellerName) {
        return entityManager.createNamedQuery("HotelBooking.findHotelByName", HotelBooking.class)
                .setParameter("travellerName", travellerName).getResultList();
    }
}
