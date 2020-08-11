package saga.eventuate.tram.hotelservice.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;

@ControllerAdvice
public class HotelExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = HotelException.class)
    protected ResponseEntity<Object> handle(HotelException exception, WebRequest webRequest) {
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
