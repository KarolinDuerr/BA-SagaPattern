package saga.microprofile.flightservice.exceptionHandler;

import saga.microprofile.flightservice.error.ErrorMessage;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.FlightServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FlightServiceExceptionMapper implements ExceptionMapper<FlightServiceException> {

    @Override
    public Response toResponse(FlightServiceException exception) {
        Status status = mapErrorTypeToResponseStatus(exception.getErrorType());
        return Response.status(status).entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
    }

    private Status mapErrorTypeToResponseStatus(final ErrorType errorType) {
        switch (errorType) {
            case INVALID_PARAMETER:
                return Status.BAD_REQUEST;
            case NON_EXISTING_ITEM:
                return Status.NOT_FOUND;
            default:
                return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
