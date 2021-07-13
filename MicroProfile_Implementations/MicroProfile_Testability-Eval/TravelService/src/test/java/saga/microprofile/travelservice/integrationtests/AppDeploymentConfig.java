package saga.microprofile.travelservice.integrationtests;

import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

public class AppDeploymentConfig implements SharedContainerConfiguration {

    @Container
    public static ApplicationContainer app = new ApplicationContainer()
            .withAppContextRoot("/")
            .withReadinessPath("/api/travel");
}
