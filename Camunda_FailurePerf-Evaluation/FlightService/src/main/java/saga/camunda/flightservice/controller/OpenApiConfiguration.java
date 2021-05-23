package saga.camunda.flightservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Flight Service",
                description = "An example for a Flight Service that enables the user to see already booked flights.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/Camunda_FailurePerf-Evaluation",
                        name = "Karolin Dürr")),
        servers = @Server(url = "http://localhost:8082")
)
@Configuration
public class OpenApiConfiguration {
}
