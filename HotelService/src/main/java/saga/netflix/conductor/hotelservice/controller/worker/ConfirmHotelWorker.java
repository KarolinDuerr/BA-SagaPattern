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
import saga.netflix.conductor.hotelservice.error.ErrorMessage;
import saga.netflix.conductor.hotelservice.error.ErrorType;

import java.util.Map;

public class ConfirmHotelWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmHotelWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;

    private final String inputConfirmHotel = HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT;

    public ConfirmHotelWorker(final ObjectMapper objectMapper, final IHotelService hotelService) {
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
    }

    @Override
    public String getTaskDefName() {
        return HotelServiceTasks.Task.CONFIRM_HOTEL;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(inputConfirmHotel)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    inputConfirmHotel);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(inputConfirmHotel));
        final BookHotelResponse bookHotelResponse =
                objectMapper.convertValue(taskInput.get(inputConfirmHotel),
                        BookHotelResponse.class);

        hotelService.confirmHotelBooking(bookHotelResponse.getBookingId(), bookHotelResponse.getTripId());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        logger.info("Hotel confirmed: " + bookHotelResponse.getBookingId()); // TODO exception berücksichtigen?

        return taskResult;
    }

}
