package saga.netflix.conductor.travelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.controller.ITravelService;
import saga.netflix.conductor.travelservice.error.ErrorMessage;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightResponse;

import java.util.Map;

public class ConfirmTripWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmTripWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ITravelService travelService;

    private final String inputHotelConfirmation = TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT;

    private final String inputFlightConfirmation = TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT;

    public ConfirmTripWorker(final ObjectMapper objectMapper, final ITravelService travelService) {
        this.objectMapper = objectMapper;
        this.travelService = travelService;
    }

    @Override
    public String getTaskDefName() {
        return TravelServiceTasks.Task.CONFIRM_TRIP;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (!validTaskInput(taskInput)) {
            String errorMessage = String.format("%s misses necessary input data.", getTaskDefName());
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        confirmTripBooking(taskInput, taskResult);
        taskResult.setStatus(TaskResult.Status.COMPLETED);

        return taskResult;
    }

    private boolean validTaskInput(final Map<String, Object> taskInput) {
        return taskInput != null && taskInput.containsKey(inputHotelConfirmation) && taskInput.containsKey(inputFlightConfirmation);
    }

    private void confirmTripBooking(Map<String, Object> taskInput, TaskResult taskResult) {
        logger.info("TaskInput for Hotel: " + taskInput.get(inputHotelConfirmation));
        final BookHotelResponse bookHotelResponse = objectMapper.convertValue(taskInput.get(inputHotelConfirmation),
                BookHotelResponse.class);

        logger.info("TaskInput for Flight: " + taskInput.get(inputFlightConfirmation));
        final BookFlightResponse bookFlightResponse =
                objectMapper.convertValue(taskInput.get(inputFlightConfirmation), BookFlightResponse.class);

        if (bookHotelResponse.getTripId() != bookFlightResponse.getTripId()) {
            logger.error("The booking responses include different trip IDs.");
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, "The booking responses " +
                    "include different trip IDs").toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return;
        }

        travelService.confirmTripBooking(bookHotelResponse.getTripId(), bookHotelResponse.getBookingId(),
                bookFlightResponse.getFlightBookingId());
        logger.info("Trip successfully confirmed: " + bookHotelResponse.getTripId()); // TODO exception berücksichtigen?
    }

}
