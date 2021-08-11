package saga.microprofile.travelservice;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(
                title = "Travel Service",
                description = "An example for a Travel Service that enables the user to book trips and to see already" +
                        " booked trips. Additionally, it also includes the LRA-Coordinator which is why its endpoints" +
                        " are also included. The different servers have already been determined for each endpoint.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile",
                        name = "Karolin Dürr")),
        servers = @Server( url = "http://localhost:8090/", description = "The server for the TravelResource endpoint.")
)
public class TravelServiceApplication extends Application {

}
