package saga.microProfile.customerservice.resources;

import saga.microProfile.customerservice.controller.CustomerServiceImpl;
import saga.microProfile.customerservice.controller.ICustomerService;
import saga.microProfile.customerservice.error.CustomerServiceException;
import saga.microProfile.customerservice.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/api/customers")
public class CustomerResource {

    private static final Logger logger = Logger.getLogger(CustomerResource.class.toString());

    @Inject
    @CustomerServiceImpl
    private ICustomerService customerService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCustomers() throws CustomerServiceException {
        logger.info("Get information about all registered customers.");

        List<Customer> customers = customerService.getCustomers();

        if (customers == null) {
            logger.info("Something went wrong during receiving the registered customers.");
            throw new WebApplicationException("Something went wrong during receiving the registered customers.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToCustomerDTOList(customers)).build();
    }


    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam(value = "customerId") final Long customerId) throws CustomerServiceException {
        logger.info("Get registered customer with ID: " + customerId);

        Customer customer = customerService.getCustomerById(customerId);

        if (customer == null) {
            logger.info("Something went wrong during receiving the registered customer.");
            throw new WebApplicationException("Something went wrong during receiving the registered customer.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToCustomerDTO(customer)).build();
    }
}
