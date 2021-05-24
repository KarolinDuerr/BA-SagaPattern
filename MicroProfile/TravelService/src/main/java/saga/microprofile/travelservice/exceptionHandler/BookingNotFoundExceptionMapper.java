package saga.microprofile.travelservice.exceptionHandler;

import saga.microprofile.travelservice.error.ErrorMessage;
import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookingNotFoundExceptionMapper implements ExceptionMapper<TravelServiceException> {

    @Override
    public Response toResponse(TravelServiceException exception) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorType.NON_EXISTING_ITEM, exception.getMessage());
        return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
    }

}
