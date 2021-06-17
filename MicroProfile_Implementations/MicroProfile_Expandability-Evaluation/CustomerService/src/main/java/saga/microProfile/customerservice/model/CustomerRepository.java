package saga.microProfile.customerservice.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class CustomerRepository {


    @PersistenceContext(name = "HotelBookingRepository")
    private EntityManager entityManager;

    @Transactional
    public void save(final Customer customer) {
        entityManager.persist(customer);
    }

    @Transactional
    public List<Customer> findAll() {
        return entityManager.createNamedQuery("Customer.findAll", Customer.class).getResultList();
    }

    @Transactional
    public Customer findById(final Long id) {
        return entityManager.find(Customer.class, id);
    }
}
