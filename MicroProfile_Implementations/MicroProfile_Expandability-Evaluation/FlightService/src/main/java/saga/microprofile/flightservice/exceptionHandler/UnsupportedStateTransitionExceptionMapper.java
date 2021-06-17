package saga.microprofile.flightservice.exceptionHandler;

import saga.microprofile.flightservice.error.ErrorMessage;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.UnsupportedStateTransition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnsupportedStateTransitionExceptionMapper implements ExceptionMapper<UnsupportedStateTransition> {

    @Override
    public Response toResponse(UnsupportedStateTransition exception) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorType.NON_ALLOWED_STATE_TRANSITION, exception.getMessage());
        return Response.status(Status.FORBIDDEN).entity(errorMessage).build();
    }



}
