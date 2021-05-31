package saga.camunda.flightservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.camunda.flightservice.model.FlightInformationRepository;
import saga.camunda.flightservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class FlightServiceConfiguration {

    @Bean
    public IFlightService flightService(FlightInformationRepository flightInformationRepository) {
        return new FlightService(flightInformationRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
