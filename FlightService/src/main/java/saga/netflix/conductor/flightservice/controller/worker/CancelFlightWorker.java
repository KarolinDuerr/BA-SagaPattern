package saga.netflix.conductor.flightservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.flightservice.controller.IFlightService;
import saga.netflix.conductor.flightservice.error.ErrorMessage;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightTask;

import java.util.Map;

public class CancelFlightWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(CancelFlightWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IFlightService flightService;


    public CancelFlightWorker(final ObjectMapper objectMapper, final IFlightService flightService) {
        this.objectMapper = objectMapper;
        this.flightService = flightService;
    }

    @Override
    public String getTaskDefName() {
        return FlightServiceTasks.Task.CANCEL_FLIGHT;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT));
        final BookFlightTask bookFlightTask =
                objectMapper.convertValue(taskInput.get(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT),
                        BookFlightTask.class);


        flightService.cancelFlightBooking(bookFlightTask.getTripId(), bookFlightTask.getTravellerName());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        logger.info("Flight successfully cancelled which is associated with Trip: " + bookFlightTask.getTripId());

        return taskResult;
    }
}
