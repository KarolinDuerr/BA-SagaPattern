package saga.microprofile.hotelservice.exceptionHandler;

import saga.microprofile.hotelservice.error.ErrorMessage;
import saga.microprofile.hotelservice.error.ErrorType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Map all {@link Throwable} exceptions to a meaningful {@link Response} so the
 * client gets a comprehensible message, so that he knows what the problem was.
 * This mapper is the last resort for occurring exceptions, to get a meaningful message.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
	    if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

	    StackTraceElement[] stacktrace = exception.getStackTrace();
		String stackTraceAppended = Arrays.stream(stacktrace).map(Object::toString).collect(Collectors.joining(",\n "));

	    String message = String.format("StackTrace: %s \n Cause: %s \n message: %s, %s", stackTraceAppended, exception.getCause(), exception.getMessage(), exception.getLocalizedMessage());
//		String message = "There has been a problem. Please check your input and try again.";
	    ErrorMessage errorMessage = new ErrorMessage(ErrorType.INTERNAL_ERROR, message);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//				.entity("There has been a problem. Please check your input and try again.").build();
				.entity(errorMessage).build();
	}

}
