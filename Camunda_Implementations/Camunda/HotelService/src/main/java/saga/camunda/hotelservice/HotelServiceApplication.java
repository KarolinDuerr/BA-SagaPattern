package saga.camunda.hotelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.camunda.hotelservice.controller.HotelServiceConfiguration;
import saga.camunda.hotelservice.controller.OpenApiConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@Import({HotelServiceConfiguration.class, OpenApiConfiguration.class})
@ComponentScan
public class HotelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);
    }
}
