package saga.eventuate.tram.eventservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Event Service",
                description = "An example for an Event Service that enables the user to see already booked events.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master" +
                                "/EventuateTram_Implementations/EventuateTram_NestedSagas/EventService",
                        name = "Karolin Dürr")),
        servers = @Server(url = "http://localhost:8082")
)
@Configuration
public class OpenApiConfiguration {
}
