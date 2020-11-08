package saga.netflix.conductor.hotelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

import java.util.Map;

public class ConfirmHotelWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmHotelWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    public ConfirmHotelWorker(final ObjectMapper objectMapper, final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return HotelServiceTasks.Task.CONFIRM_HOTEL;
    }

    @Override
    public TaskResult execute(final Task task) { // TODO refactoring?
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT)) {
            logger.info(String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT));
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT));
        final BookHotelResponse bookHotelResponse =
                objectMapper.convertValue(taskInput.get(HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT),
                        BookHotelResponse.class);

        hotelService.confirmHotelBooking(bookHotelResponse.getBookingId(), bookHotelResponse.getTripId());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        logger.info("Hotel confirmed: " + bookHotelResponse.getBookingId()); // TODO exception berücksichtigen?

        return taskResult;
    }

}
