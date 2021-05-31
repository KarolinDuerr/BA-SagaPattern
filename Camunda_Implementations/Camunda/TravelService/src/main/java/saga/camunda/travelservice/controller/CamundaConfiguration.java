package saga.camunda.travelservice.controller;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import saga.camunda.travelservice.model.TripInformationRepository;
import saga.camunda.travelservice.resources.DtoConverter;
import saga.camunda.travelservice.saga.BookTripSaga;

@Configuration
public class CamundaConfiguration {

//    @Bean
//    public ProcessEngine processEngine() {
//        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
//        return processEngine;
//    }

    @Autowired
    private CamundaBpmProperties camundaBpmProperties;

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public BookTripSaga bookTripSaga(final ProcessEngine processEngine) {
        return new BookTripSaga(processEngine);
    }
}
