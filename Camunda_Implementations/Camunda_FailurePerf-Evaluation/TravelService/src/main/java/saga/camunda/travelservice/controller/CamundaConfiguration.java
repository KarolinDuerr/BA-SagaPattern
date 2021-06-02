package saga.camunda.travelservice.controller;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.camunda.travelservice.saga.BookTripSaga;

@Configuration
public class CamundaConfiguration {

    @Autowired
    private CamundaBpmProperties camundaBpmProperties;

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public BookTripSaga bookTripSaga(final ProcessEngine processEngine) {
        return new BookTripSaga(processEngine);
    }
}
