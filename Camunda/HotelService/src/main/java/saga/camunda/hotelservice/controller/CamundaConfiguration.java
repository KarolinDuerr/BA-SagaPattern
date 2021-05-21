package saga.camunda.hotelservice.controller;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.camunda.hotelservice.api.HotelServiceChannels;

@Configuration
public class CamundaConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CamundaConfiguration.class);

    @Bean
    @ExternalTaskSubscription("bookHotel")
    public ExternalTaskHandler bookHotelHandler() {
        logger.info("HotelService received a task");
        return (externalTask, externalTaskService) -> {
            // TODO
            logger.info("Task: " + externalTask.getVariable(HotelServiceChannels.DataInput.BOOK_HOTEL_DATA));
            externalTaskService.complete(externalTask);
        };
    }
}
