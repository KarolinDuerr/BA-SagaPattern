package saga.eventuate.tram.customerservice.controller;

import saga.eventuate.tram.customerservice.error.CustomerException;
import saga.eventuate.tram.customerservice.model.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(final long customerId) throws CustomerException;

    // to have example entries in the database
    void provideExampleEntries();
}
