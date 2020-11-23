package saga.eventuate.tram.customerservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.customerservice.error.CustomerException;
import saga.eventuate.tram.customerservice.error.ErrorType;
import saga.eventuate.tram.customerservice.model.Address;
import saga.eventuate.tram.customerservice.model.Customer;
import saga.eventuate.tram.customerservice.model.CustomerRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("CustomerService")
@Transactional
public class CustomerService implements ICustomerService{

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getCustomers() {
        logger.info("Get registered customers from repository.");

        List<Customer> customers = new LinkedList<>();

        Iterable<Customer> savedCustomers = customerRepository.findAll();

        for (Customer customer : savedCustomers) {
            customers.add(customer);
        }

        return customers;
    }

    @Override
    public Customer getCustomerById(long customerId) throws CustomerException {
        logger.info(String.format("Get registered customer (ID: %d) from Repository.", customerId));

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (!customer.isPresent()) {
            String message = String.format("The flight information (ID: %d) does not exist.", customerId);
            logger.info(message);
            throw new CustomerException(ErrorType.NON_EXISTING_CUSTOMER, message);
        }

        return customer.get();
    }

    @Override
    // to provide information which can be shown as no customers can be registered in the example application
    public void provideExampleEntries() {
        Address maxAddress = new Address("Germany", "Bamberg", "Kapellenstraße", 13);
        Customer maxCustomer = new Customer("Max Mustermann", maxAddress, "max.mustermann@web.de");

        Address alexAddress = new Address("Scotland", "Stirling", "Hermitage Road", 9);
        Customer alexCustomer = new Customer("Alex Mustermann", alexAddress, "alex.mustermann@web.de");

        Address peterAddress = new Address("USA", "New York", "10th Ave, Brooklyn", 1603);
        Customer peterCustomer = new Customer("Peter Mustermann", peterAddress, "peter.mustermann@web.de");

        customerRepository.save(maxCustomer);
        customerRepository.save(alexCustomer);
        customerRepository.save(peterCustomer);
    }
}
