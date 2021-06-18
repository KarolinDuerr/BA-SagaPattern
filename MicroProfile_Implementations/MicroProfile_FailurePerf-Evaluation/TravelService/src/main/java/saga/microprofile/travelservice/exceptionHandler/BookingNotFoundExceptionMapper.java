package saga.microprofile.travelservice.exceptionHandler;

import saga.microprofile.travelservice.error.BookingNotFound;
import saga.microprofile.travelservice.error.ErrorMessage;
import saga.microprofile.travelservice.error.ErrorType;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookingNotFoundExceptionMapper implements ExceptionMapper<BookingNotFound> {

    @Override
    public Response toResponse(BookingNotFound exception) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorType.NON_EXISTING_ITEM, exception.getMessage());
        return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
    }

}
