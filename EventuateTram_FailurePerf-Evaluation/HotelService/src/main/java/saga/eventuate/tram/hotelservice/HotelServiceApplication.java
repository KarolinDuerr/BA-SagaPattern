package saga.eventuate.tram.hotelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.eventuate.tram.hotelservice.controller.HotelServiceConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@Import(HotelServiceConfiguration.class)
@ComponentScan
public class HotelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);
    }
}
