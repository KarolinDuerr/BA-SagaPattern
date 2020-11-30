package saga.eventuate.tram.hotelservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Hotel Service",
                description = "An example for a Hotel service that enables the user to see already booked hotels.",
                version = "1.0.0",
                contact = @Contact(url = "https://github.com/KarolinDuerr/BA-SagaPattern", name = "Karolin Dürr")),
        servers = @Server(url = "http//localhost:8081")
)
@Configuration
public class OpenApiConfiguration {
}
