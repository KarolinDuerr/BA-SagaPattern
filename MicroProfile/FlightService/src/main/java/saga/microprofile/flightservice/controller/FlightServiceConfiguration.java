package saga.microprofile.flightservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.microprofile.flightservice.model.FlightInformationRepository;
import saga.microprofile.flightservice.resources.DtoConverter;

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
