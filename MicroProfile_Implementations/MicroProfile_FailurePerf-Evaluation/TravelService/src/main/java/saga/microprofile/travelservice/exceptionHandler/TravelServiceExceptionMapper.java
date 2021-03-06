package saga.microprofile.travelservice.exceptionHandler;

import saga.microprofile.travelservice.error.ErrorMessage;
import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TravelServiceExceptionMapper implements ExceptionMapper<TravelServiceException> {

    @Override
    public Response toResponse(TravelServiceException exception) {
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
