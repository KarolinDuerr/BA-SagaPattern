package saga.camunda.hotelservice.controller.worker;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.travelservice.api.TravelServiceTopics;

@Component
@ExternalTaskSubscription(value = "cancelHotel", processDefinitionKey = TravelServiceTopics.Sagas.BOOK_TRIP_SAGA)
public class CancelHotelWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(CancelHotelWorker.class);

    @Autowired
    private final IHotelService hotelService;

    public CancelHotelWorker(final IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Override
    public void execute(final ExternalTask externalTask, final ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookHotelRequest bookHotelRequest = externalTask.getVariable(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA);

        if (bookHotelRequest == null) {
            logger.info("The given input could not be parsed to a bookHotelRequest.");
            externalTaskService.handleBpmnError(externalTask, HotelServiceTopics.BpmnError.HOTEL_ERROR, "Something went" +
                    " wrong with the given input.");
            return;
        }

        hotelService.cancelHotelBooking(bookHotelRequest.getTripId(), bookHotelRequest.getTravellerName());
        logger.info("Hotel successfully cancelled which is associated with Trip: " + bookHotelRequest.getTripId());

        externalTaskService.complete(externalTask);
        logger.debug("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }
}
