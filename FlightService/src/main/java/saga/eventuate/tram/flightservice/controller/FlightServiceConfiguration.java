package saga.eventuate.tram.flightservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.eventuate.tram.flightservice.model.FlightInformationRepository;
import saga.eventuate.tram.flightservice.resources.DtoConverter;

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
