package saga.camunda.travelservice.controller;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.camunda.travelservice.model.TripInformationRepository;
import saga.camunda.travelservice.resources.DtoConverter;
import saga.camunda.travelservice.saga.BookTripSaga;

@Configuration
@EnableAutoConfiguration
@Import(CamundaConfiguration.class)
public class TravelServiceConfiguration {

    @Bean
    public ITravelService travelService(final TripInformationRepository tripInformationRepository, final BookTripSaga bookTripSaga) {
        return new TravelService(tripInformationRepository, bookTripSaga);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
