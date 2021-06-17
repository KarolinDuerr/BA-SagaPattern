package saga.microProfile.customerservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import saga.microProfile.customerservice.controller.CustomerServiceImpl;
import saga.microProfile.customerservice.controller.ICustomerService;
import saga.microProfile.customerservice.error.CustomerServiceException;
import saga.microProfile.customerservice.error.ErrorMessage;
import saga.microProfile.customerservice.error.ErrorType;
import saga.microProfile.customerservice.model.Customer;
import saga.microprofile.customerservice.api.dto.CustomerValidationFailed;
import saga.microprofile.customerservice.api.dto.ValidateCustomerRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_RECOVERY_HEADER;

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
            throw new WebApplicationException("Something went wrong during receiving the registered customers.",
                    Response.Status.INTERNAL_SERVER_ERROR);
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
            throw new WebApplicationException("Something went wrong during receiving the registered customer.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToCustomerDTO(customer)).build();
    }

    @LRA(value = LRA.Type.MANDATORY, cancelOn = {Response.Status.INTERNAL_SERVER_ERROR}, cancelOnFamily =
            {Response.Status.Family.CLIENT_ERROR}, end = false)
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateCustomer(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                     @RequestBody ValidateCustomerRequest validateCustomerRequest) {
        logger.info("Received ValidateCustomerRequest: " + validateCustomerRequest + " with LRA (ID: " + lraId + ")");

        try {
            customerService.validateCustomer(validateCustomerRequest.getCustomerId());
            return Response.ok().build();
        } catch (CustomerServiceException exception) {
            logger.warning(exception.toString());

            if (exception.getErrorType() == ErrorType.CUSTOMER_VALIDATION_FAILED) {
                return Response.serverError().entity(new CustomerValidationFailed()).build();
            }

            return Response.serverError().entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
        }
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response cancelFlight(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) final URI lraId,
                                 @HeaderParam(LRA_HTTP_RECOVERY_HEADER) final URI recoveryId) {
        logger.info("Compensate LRA (ID: " + lraId + ") with the following recovery ID: " + recoveryId.toString());
        /** no compensation needed since the validation of a customer didn't change anything, but compensation method
         * is still needed for the coordinator
         */
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) final URI lraId) {
        logger.info("Completing LRA (ID: " + lraId + ")");
        return Response.ok(ParticipantStatus.Completed).build();
    }
}
