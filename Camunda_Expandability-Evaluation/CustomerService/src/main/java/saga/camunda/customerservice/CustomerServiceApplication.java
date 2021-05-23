package saga.camunda.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import saga.camunda.customerservice.controller.CustomerServiceConfiguration;
import saga.camunda.customerservice.controller.OpenApiConfiguration;

@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@Import({CustomerServiceConfiguration.class, OpenApiConfiguration.class})
@ComponentScan
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
