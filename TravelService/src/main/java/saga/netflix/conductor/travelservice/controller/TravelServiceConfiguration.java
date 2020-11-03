package saga.netflix.conductor.travelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.netflix.conductor.travelservice.model.TripInformationRepository;
import saga.netflix.conductor.travelservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class TravelServiceConfiguration {

    @Bean
    public ITravelService travelService(TripInformationRepository tripInformationRepository) {
        return new TravelService(tripInformationRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
