package saga.camunda.travelservice;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.camunda.travelservice.controller.OpenApiConfiguration;
import saga.camunda.travelservice.controller.TravelServiceConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
//@EnableProcessApplication("processApplication") // TODO use?
@Import({TravelServiceConfiguration.class, OpenApiConfiguration.class})
@ComponentScan
public class TravelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServiceApplication.class, args);
    }
}
