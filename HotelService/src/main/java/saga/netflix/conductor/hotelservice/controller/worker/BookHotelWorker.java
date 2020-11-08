package saga.netflix.conductor.hotelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelRequest;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.error.HotelServiceException;
import saga.netflix.conductor.hotelservice.model.HotelBooking;
import saga.netflix.conductor.hotelservice.model.HotelBookingInformation;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

import java.util.Map;

public class BookHotelWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    public BookHotelWorker(final ObjectMapper objectMapper, final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return HotelServiceTasks.Task.BOOK_HOTEL;
    }

    @Override
    public TaskResult execute(final Task task) { // TODO refactoring
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT)) {
            logger.info(String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
        final BookHotelRequest bookHotelRequest =
                objectMapper.convertValue(taskInput.get(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT),
                        BookHotelRequest.class);

        try {
            HotelBookingInformation bookingInformation =
                    dtoConverter.convertToHotelBookingInformation(bookHotelRequest);

            HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

            BookHotelResponse bookingResponse = new BookHotelResponse(bookHotelRequest.getTripId(), hotelBooking.getId(),
                    hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

            taskResult.getOutputData().put(HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT, bookingResponse);
            taskResult.setStatus(TaskResult.Status.COMPLETED);
            logger.info("Hotel successfully booked: " + bookingResponse);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            taskResult.setReasonForIncompletion(exception.toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
        }

        return taskResult;
    }

}
