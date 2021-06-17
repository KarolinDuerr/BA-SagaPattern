package saga.microProfile.customerservice;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(
                title = "Customer Service",
                description = "An example for a Customer Service that enables the user to see registered customers.",
                version = "1.0.0",
                contact = @Contact(
                        url = "https://github.com/KarolinDuerr/BA-SagaPattern/tree/master" +
                                "/MicroProfile_Implementations/MicroProfile_Expandability-Evaluation",
                        name = "Karolin Dürr"))
)
@ApplicationPath("/")
public class CustomerServiceApplication extends Application {

}
