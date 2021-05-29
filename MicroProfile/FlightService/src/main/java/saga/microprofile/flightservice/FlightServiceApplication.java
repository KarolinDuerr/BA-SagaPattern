package saga.microprofile.flightservice;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(
                title = "Flight Service",
                description = "An example for a Flight Service that enables the user to see already booked flights.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile",
                        name = "Karolin Dürr"))
)
public class FlightServiceApplication extends Application {

}
