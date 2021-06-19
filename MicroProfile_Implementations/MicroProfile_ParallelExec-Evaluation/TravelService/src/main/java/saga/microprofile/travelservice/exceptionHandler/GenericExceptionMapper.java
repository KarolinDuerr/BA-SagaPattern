package saga.microprofile.travelservice.exceptionHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity("There has been a problem. Please check your input and try again.").build();
	}

}
