package saga.microprofile.flightservice.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saga.microprofile.flightservice.error.ErrorMessage;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.FlightServiceException;

@ControllerAdvice
public class FlightServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = FlightServiceException.class)
    protected ResponseEntity<Object> handle(final FlightServiceException exception, final WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getErrorType(), exception.getMessage());
        return handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
                mapErrorTypeToResponseStatus(exception.getErrorType()), webRequest);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    protected ResponseEntity<Object> handle(final ResponseStatusException exception, final WebRequest webRequest) {
        throw exception;
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handle(final Exception exception, final WebRequest webRequest) {
        logger.error("An exception occurred: " + exception.getMessage() + ", of type " + exception.getClass() + " " +
                "with the following cause: " + exception.getCause());
        return handleExceptionInternal(exception, "There has been an error, please check your input and try again.",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    private HttpStatus mapErrorTypeToResponseStatus(final ErrorType errorType) {
        switch (errorType) {
            case INVALID_PARAMETER:
                return HttpStatus.BAD_REQUEST;
            case NON_EXISTING_ITEM:
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
