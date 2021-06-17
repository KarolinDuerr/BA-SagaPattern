package saga.microProfile.customerservice.exceptionHandler;

import saga.microProfile.customerservice.error.CustomerServiceException;
import saga.microProfile.customerservice.error.ErrorMessage;
import saga.microProfile.customerservice.error.ErrorType;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomerServiceExceptionMapper implements ExceptionMapper<CustomerServiceException> {

    @Override
    public Response toResponse(CustomerServiceException exception) {
        Status status = mapErrorTypeToResponseStatus(exception.getErrorType());
        return Response.status(status).entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
    }

    private Status mapErrorTypeToResponseStatus(final ErrorType errorType) {
        switch (errorType) {
            case INVALID_PARAMETER:
                return Status.BAD_REQUEST;
            case NON_EXISTING_CUSTOMER:
                return Status.NOT_FOUND;
            default:
                return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
