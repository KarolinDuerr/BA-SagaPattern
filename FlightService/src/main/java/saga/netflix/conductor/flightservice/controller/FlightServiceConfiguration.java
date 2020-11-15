package saga.netflix.conductor.flightservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.netflix.conductor.flightservice.model.FlightInformationRepository;
import saga.netflix.conductor.flightservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
@Import(ConductorConfiguration.class)
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
