package saga.camunda.customerservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Service",
                description = "An example for a Customer Service that enables the user to see registered customers.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/Camunda_Implementations" +
                                "/Camunda_Expandability-Evaluation",
                        name = "Karolin Dürr")),
        servers = @Server(url = "http://localhost:8083")
)
@Configuration
public class OpenApiConfiguration {
}
