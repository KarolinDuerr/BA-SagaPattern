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
import saga.camunda.hotelservice.api.dto.BookHotelResponse;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.hotelservice.error.ErrorType;

@Component
@ExternalTaskSubscription("confirmHotel")
public class ConfirmHotelWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmHotelWorker.class);

    @Autowired
    private final IHotelService hotelService;

    public ConfirmHotelWorker(final IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookHotelResponse bookHotelResponse = externalTask.getVariable(HotelServiceTopics.DataOutput.BOOK_HOTEL_RESPONSE);

        if (bookHotelResponse == null) {
            logger.info("The given input could not be used to confirm the hotel booking.");
            externalTaskService.handleBpmnError(externalTask, ErrorType.INVALID_PARAMETER.toString(), "Something went" +
                    " wrong with the given input."); // TODO
            return;
        }

        hotelService.confirmHotelBooking(bookHotelResponse.getBookingId(), bookHotelResponse.getTripId());
        logger.info("Hotel confirmed: " + bookHotelResponse.getBookingId());

        externalTaskService.complete(externalTask);
        logger.info("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }
}
