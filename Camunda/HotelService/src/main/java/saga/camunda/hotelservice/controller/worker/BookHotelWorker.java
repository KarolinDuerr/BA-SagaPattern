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
import saga.camunda.hotelservice.api.dto.BookHotelResponse;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.hotelservice.error.ErrorType;
import saga.camunda.hotelservice.error.HotelServiceException;
import saga.camunda.hotelservice.model.HotelBooking;
import saga.camunda.hotelservice.model.HotelBookingInformation;
import saga.camunda.hotelservice.resources.DtoConverter;

@Component
@ExternalTaskSubscription("bookHotel")
public class BookHotelWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelWorker.class);

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    public BookHotelWorker(final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookHotelRequest bookHotelRequest = externalTask.getVariable(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA);

        if (bookHotelRequest == null) {
            logger.info("The given input could not be parsed to a bookHotelRequest.");
            externalTaskService.handleBpmnError(externalTask, ErrorType.INVALID_PARAMETER.toString(), "Something went" +
                    " wrong with the given input."); // TODO
        }

        try {
            bookHotel(bookHotelRequest, externalTask, externalTaskService);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            externalTaskService.handleBpmnError(externalTask, exception.getErrorType().toString(),
                    exception.getMessage()); // TODO
//            externalTaskService.handleFailure(externalTask, exception.getMessage(), exception.getErrorType().toString(), 0, 5000);
        }

        logger.info("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }

    private void bookHotel(final BookHotelRequest bookHotelRequest, final ExternalTask externalTask,
                          final ExternalTaskService externalTaskService) throws HotelServiceException {
        HotelBookingInformation bookingInformation =
                dtoConverter.convertToHotelBookingInformation(bookHotelRequest);

        HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

        BookHotelResponse bookingResponse = new BookHotelResponse(bookHotelRequest.getTripId(), hotelBooking.getId(),
                hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

//        Map<String, Object> variables = new HashMap<>();
//        variables.put(HotelServiceTopics.DataOutput.BOOK_HOTEL_RESPONSE, bookingResponse);

//        externalTaskService.complete(externalTask, variables);
        externalTaskService.complete(externalTask);
        logger.info("Hotel successfully booked: " + bookingResponse);
    }
}
