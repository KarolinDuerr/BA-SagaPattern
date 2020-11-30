package saga.eventuate.tram.travelservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Travel Service",
                description = "An example for a Travel Service that enables the user to book trips and to see already booked trips.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/EventuateTram_Expandability-Evaluation",
                        name = "Karolin Dürr"))
)
@Configuration
public class OpenApiConfiguration {
}
