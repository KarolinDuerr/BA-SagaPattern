package saga.eventuate.tram.travelservice.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saga.eventuate.tram.travelservice.error.ErrorType;
import saga.eventuate.tram.travelservice.error.TravelException;

@ControllerAdvice
public class TravelExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TravelException.class)
    protected ResponseEntity<Object> handle(TravelException exception, WebRequest webRequest) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
                mapErrorTypeToResponseStatus(exception.getErrorType()), webRequest);
    }

    private HttpStatus mapErrorTypeToResponseStatus(ErrorType errorType) {
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
