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
import saga.netlfix.conductor.flightservice.api.dto.BookFlightRequest;

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
            // prevent retry --> input would still be missing, so no reason to retry
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
            return taskResult;
        }

        logger.info("TaskInput: " + taskInput.get(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT));
        final BookFlightRequest bookFlightRequest =
                objectMapper.convertValue(taskInput.get(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT),
                        BookFlightRequest.class);


        flightService.cancelFlightBooking(bookFlightRequest.getTripId(), bookFlightRequest.getTravellerName());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        logger.info("Flight successfully cancelled which is associated with Trip: " + bookFlightRequest.getTripId());

        return taskResult;
    }
}
