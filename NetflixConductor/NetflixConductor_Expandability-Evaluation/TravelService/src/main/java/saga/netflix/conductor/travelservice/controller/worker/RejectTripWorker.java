package saga.netflix.conductor.travelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelRequest;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.controller.ITravelService;
import saga.netflix.conductor.travelservice.error.ErrorMessage;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netflix.conductor.travelservice.model.RejectionReason;

import java.util.Map;

public class RejectTripWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(RejectTripWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ITravelService travelService;

    private final String inputTripID = TravelServiceTasks.TaskInput.REJECT_TRIP_ID_INPUT;

    private final String inputReason = TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT;

    public RejectTripWorker(final ObjectMapper objectMapper, final ITravelService travelService) {
        this.objectMapper = objectMapper;
        this.travelService = travelService;
    }

    @Override
    public String getTaskDefName() {
        return TravelServiceTasks.Task.REJECT_TRIP;
    }

    @Override
    public TaskResult execute(Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);
        Map<String, Object> taskInput = task.getInputData();

        if (!validTaskInput(taskInput)) {
            String errorMessage = String.format("%s misses necessary input data.", getTaskDefName());
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            // prevent retry --> input would still be missing, so no reason to retry
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
            return taskResult;
        }

        rejectTrip(taskInput);
        taskResult.setStatus(TaskResult.Status.COMPLETED);

        return taskResult;
    }

    private boolean validTaskInput(final Map<String, Object> taskInput) {
        return taskInput != null && taskInput.containsKey(inputReason) && taskInput.containsKey(inputTripID);
    }

    private void rejectTrip(final Map<String, Object> taskInput) {
        logger.info("TaskInput bookHotelRequest: " + taskInput.get(inputTripID));
        final BookHotelRequest bookHotelRequest =
                objectMapper.convertValue(taskInput.get(inputTripID),
                        BookHotelRequest.class);

        logger.info("TaskInput reason: " + taskInput.get(inputReason));
        final String reason =
                objectMapper.convertValue(taskInput.get(inputReason),
                        String.class);

        RejectionReason rejectionReason = getRejectionReason(reason);
        travelService.rejectTrip(bookHotelRequest.getTripId(), rejectionReason);
    }

    private RejectionReason getRejectionReason(final String reason) {
        if (reason.contains(RejectionReason.NO_HOTEL_AVAILABLE.toString())) {
            return RejectionReason.NO_HOTEL_AVAILABLE;
        } else if (reason.contains(RejectionReason.NO_FLIGHT_AVAILABLE.toString())) {
            return RejectionReason.NO_FLIGHT_AVAILABLE;
        } else if (reason.contains(RejectionReason.CUSTOMER_VALIDATION_FAILED.toString())) {
            return RejectionReason.CUSTOMER_VALIDATION_FAILED;
        }
        return RejectionReason.REASON_UNKNOWN;
    }
}
