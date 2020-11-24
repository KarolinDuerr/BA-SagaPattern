package saga.eventuate.tram.travelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.eventuate.tram.travelservice.controller.TravelServiceConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@Import(TravelServiceConfiguration.class)
@ComponentScan
public class TravelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServiceApplication.class, args);
    }
}
