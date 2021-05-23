package saga.camunda.customerservice.resources;

import saga.camunda.customerservice.error.ConverterException;
import saga.camunda.customerservice.error.ErrorType;
import saga.camunda.customerservice.model.Customer;
import saga.camunda.customerservice.model.dto.CustomerDTO;

import java.util.LinkedList;
import java.util.List;

public class DtoConverter {

    public List<CustomerDTO> convertToCustomerDTOList(final List<Customer> customers) throws ConverterException {
        if (customers == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The registered customers could not be received.");
        }

        List<CustomerDTO> customersDTOs = new LinkedList<>();

        for (Customer customer : customers) {
            customersDTOs.add(convertToCustomerDTO(customer));
        }

        return customersDTOs;
    }

    public CustomerDTO convertToCustomerDTO(final Customer customer) throws ConverterException {
        if (customer == null) {
            throw new ConverterException(ErrorType.INTERNAL_ERROR, "The registered customer is missing.");
        }

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getEMail());
    }
}
