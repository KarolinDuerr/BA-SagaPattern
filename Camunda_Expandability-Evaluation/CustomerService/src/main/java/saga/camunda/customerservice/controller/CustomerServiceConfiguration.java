package saga.camunda.customerservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.camunda.customerservice.model.CustomerRepository;
import saga.camunda.customerservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class CustomerServiceConfiguration {

    @Bean
    public ICustomerService customerService(CustomerRepository customerRepository) {
        CustomerService customerService = new CustomerService(customerRepository);
        customerService.provideExampleEntries();
        return customerService;
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
