package saga.eventuate.tram.customerservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.eventuate.tram.customerservice.model.CustomerRepository;
import saga.eventuate.tram.customerservice.resources.DtoConverter;

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
