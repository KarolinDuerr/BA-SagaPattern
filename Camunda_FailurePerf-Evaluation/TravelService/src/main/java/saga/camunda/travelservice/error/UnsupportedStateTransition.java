package saga.camunda.travelservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "This state transition is not allowed.")
public class UnsupportedStateTransition extends RuntimeException {

    public UnsupportedStateTransition(final String message) {
        super(message);
    }
}
