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
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.error.ErrorMessage;
import saga.netflix.conductor.hotelservice.error.ErrorType;

import java.util.Map;

public class CancelHotelWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(CancelHotelWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;


    public CancelHotelWorker(final ObjectMapper objectMapper, final IHotelService hotelService) {
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
    }

    @Override
    public String getTaskDefName() {
        return HotelServiceTasks.Task.CANCEL_HOTEL;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT));
        final BookHotelRequest bookHotelRequest =
                objectMapper.convertValue(taskInput.get(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT),
                        BookHotelRequest.class);


        hotelService.cancelHotelBooking(bookHotelRequest.getTripId(), bookHotelRequest.getTravellerName());
        taskResult.setStatus(TaskResult.Status.COMPLETED);

        return taskResult;
    }
}
