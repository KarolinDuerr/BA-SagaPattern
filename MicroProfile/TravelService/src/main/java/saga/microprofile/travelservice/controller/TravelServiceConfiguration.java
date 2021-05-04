package saga.microprofile.travelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.microprofile.travelservice.model.TripInformationRepository;
import saga.microprofile.travelservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class TravelServiceConfiguration {

    @Bean
    public ITravelService travelService(TripInformationRepository tripInformationRepository) { // TODO
        return new TravelService(tripInformationRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
