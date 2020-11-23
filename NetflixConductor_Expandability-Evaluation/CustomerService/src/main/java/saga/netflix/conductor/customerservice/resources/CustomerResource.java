package saga.netflix.conductor.customerservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import saga.netflix.conductor.customerservice.controller.ICustomerService;
import saga.netflix.conductor.customerservice.error.CustomerServiceException;
import saga.netflix.conductor.customerservice.model.Customer;
import saga.netflix.conductor.customerservice.model.dto.CustomerDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customers")
public class CustomerResource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private DtoConverter dtoConverter;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() throws CustomerServiceException {
        logger.info("Get information about all registered customers.");

        List<Customer> customers = customerService.getCustomers();

        if (customers == null) {
            logger.info("Something went wrong during receiving the registered customers.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the registered customers.");
        }

        return ResponseEntity.ok(dtoConverter.convertToCustomerDTOList(customers));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable(value = "customerId") final Long customerId) throws CustomerServiceException {
        logger.info("Get registered customer with ID: " + customerId);

        Customer customer = customerService.getCustomerById(customerId);

        if (customer == null) {
            logger.info("Something went wrong during receiving the registered customer.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the registered customer.");
        }

        return ResponseEntity.ok(dtoConverter.convertToCustomerDTO(customer));
    }
}
