package saga.camunda.customerservice.controller;

import saga.camunda.customerservice.error.CustomerException;
import saga.camunda.customerservice.model.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(final long customerId) throws CustomerException;

    void validateCustomer(final long customerId) throws CustomerException;

    // to have example entries in the database
    void provideExampleEntries();
}
