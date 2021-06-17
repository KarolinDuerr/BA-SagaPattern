package saga.microProfile.customerservice.controller;

import saga.microProfile.customerservice.error.CustomerException;
import saga.microProfile.customerservice.error.ErrorType;
import saga.microProfile.customerservice.model.Address;
import saga.microProfile.customerservice.model.Customer;
import saga.microProfile.customerservice.model.CustomerRepository;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PostLoad;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@CustomerServiceImpl
public class CustomerService implements ICustomerService{

    private static final Logger logger = Logger.getLogger(CustomerService.class.toString());

    @Inject
    private CustomerRepository customerRepository;

    public CustomerService() {
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

        Customer customer = customerRepository.findById(customerId);

        if (customer == null) {
            String message = String.format("The flight information (ID: %d) does not exist.", customerId);
            logger.info(message);
            throw new CustomerException(ErrorType.NON_EXISTING_CUSTOMER, message);
        }

        return customer;
    }

    @Override
    // only mocking the task of this method
    public void validateCustomer(long customerId) throws CustomerException {
        if (customerId < 1) {
            String message = String.format("The customer (ID: %d) does not exist.", customerId);
            logger.info(message);
            throw new CustomerException(ErrorType.CUSTOMER_VALIDATION_FAILED, message);
        }
        logger.info("Validation of customer successful, customerID: " + customerId);
    }

    @PostConstruct
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
