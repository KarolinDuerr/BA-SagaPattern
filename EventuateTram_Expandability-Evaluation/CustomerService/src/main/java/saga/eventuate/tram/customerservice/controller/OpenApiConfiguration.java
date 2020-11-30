package saga.eventuate.tram.customerservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Service",
                description = "An example for a Customer Service that enables the user to see registered customers.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/EventuateTram_Expandability-Evaluation",
                        name = "Karolin Dürr"))
)
@Configuration
public class OpenApiConfiguration {
}
