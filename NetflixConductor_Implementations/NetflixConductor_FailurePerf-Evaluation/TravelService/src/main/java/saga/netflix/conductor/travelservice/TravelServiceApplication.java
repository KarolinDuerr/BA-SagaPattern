package saga.netflix.conductor.travelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.netflix.conductor.travelservice.controller.OpenApiConfiguration;
import saga.netflix.conductor.travelservice.controller.TravelServiceConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@Import({TravelServiceConfiguration.class, OpenApiConfiguration.class})
@ComponentScan
public class TravelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServiceApplication.class, args);
    }
}
