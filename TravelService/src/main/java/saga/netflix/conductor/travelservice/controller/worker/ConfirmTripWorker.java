package saga.netflix.conductor.travelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.controller.ITravelService;
import saga.netflix.conductor.travelservice.error.ErrorMessage;
import saga.netflix.conductor.travelservice.error.ErrorType;
import saga.netflix.conductor.travelservice.resources.DtoConverter;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightResponse;

import java.util.Map;

public class ConfirmTripWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmTripWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ITravelService travelService;

    @Autowired
    private final DtoConverter dtoConverter;

    public ConfirmTripWorker(final ObjectMapper objectMapper, final ITravelService travelService,
                             final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.travelService = travelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return TravelServiceTasks.Task.CONFIRM_TRIP;
    }

    @Override
    public TaskResult execute(final Task task) { // TODO refactoring
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT) || !taskInput.containsKey(TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT)) {
            logger.info(String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
            taskResult.setStatus(TaskResult.Status.FAILED); // TODO beide Fälle berücksichtigen und Grund angeben
            return taskResult;
        }

        logger.info("TaskInput for Hotel: " + taskInput.get(TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT));
        final BookHotelResponse bookHotelResponse =
                objectMapper.convertValue(taskInput.get(TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT),
                        BookHotelResponse.class);

        logger.info("TaskInput for Flight: " + taskInput.get(TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT));
        final BookFlightResponse bookFlightResponse =
                objectMapper.convertValue(taskInput.get(TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT),
                        BookFlightResponse.class);

        if (bookHotelResponse.getTripId() != bookFlightResponse.getTripId()) {
            logger.error("The booking response include different trip IDs");
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, "The booking response include different trip IDs").toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        travelService.confirmTripBooking(bookHotelResponse.getTripId(), bookHotelResponse.getBookingId(), bookFlightResponse.getFlightBookingId());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        logger.info("Trip successfully confirmed: " + bookHotelResponse.getTripId()); // TODO exception berücksichtigen?

        return taskResult;
    }

}
