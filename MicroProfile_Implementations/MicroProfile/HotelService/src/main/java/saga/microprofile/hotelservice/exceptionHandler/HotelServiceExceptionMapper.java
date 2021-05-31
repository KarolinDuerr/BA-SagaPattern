package saga.microprofile.hotelservice.exceptionHandler;

import saga.microprofile.hotelservice.error.ErrorMessage;
import saga.microprofile.hotelservice.error.ErrorType;
import saga.microprofile.hotelservice.error.HotelServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HotelServiceExceptionMapper implements ExceptionMapper<HotelServiceException> {

    @Override
    public Response toResponse(HotelServiceException exception) {
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
