package saga.microprofile.hotelservice.exceptionHandler;

import saga.microprofile.hotelservice.error.ErrorMessage;
import saga.microprofile.hotelservice.error.ErrorType;
import saga.microprofile.hotelservice.error.UnsupportedStateTransition;

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
