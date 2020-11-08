package saga.netflix.conductor.travelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.netflix.conductor.travelservice.model.TripInformationRepository;
import saga.netflix.conductor.travelservice.resources.DtoConverter;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;

@Configuration
@EnableAutoConfiguration
@Import(ConductorConfiguration.class)
public class TravelServiceConfiguration {

    @Bean
    public ITravelService travelService(TripInformationRepository tripInformationRepository, SagaInstanceFactory sagaInstanceFactory) {
        return new TravelService(tripInformationRepository, sagaInstanceFactory);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
