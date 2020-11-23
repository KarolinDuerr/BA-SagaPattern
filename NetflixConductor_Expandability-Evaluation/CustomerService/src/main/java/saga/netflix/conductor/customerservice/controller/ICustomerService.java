package saga.netflix.conductor.customerservice.controller;

import saga.netflix.conductor.customerservice.error.CustomerException;
import saga.netflix.conductor.customerservice.model.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(final long customerId) throws CustomerException;

    // to have example entries in the database
    void provideExampleEntries();
}
