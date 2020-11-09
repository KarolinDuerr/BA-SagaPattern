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
import saga.netflix.conductor.flightservice.error.FlightServiceException;
import saga.netflix.conductor.flightservice.model.FindAndBookFlightInformation;
import saga.netflix.conductor.flightservice.model.FlightInformation;
import saga.netflix.conductor.flightservice.resources.DtoConverter;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightResponse;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightTask;

import java.util.Map;

public class BookFlightWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(BookFlightWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    private final String inputBookFlight = FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT;

    public BookFlightWorker(final ObjectMapper objectMapper, final IFlightService flightService,
                            final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return FlightServiceTasks.Task.BOOK_FLIGHT;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(inputBookFlight)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    inputBookFlight);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
            return taskResult;
        }

        try {
            bookFlight(taskInput, taskResult);
            taskResult.setStatus(TaskResult.Status.COMPLETED);
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());
            taskResult.setReasonForIncompletion(exception.toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
        }

        return taskResult;
    }

    private void bookFlight(final Map<String, Object> taskInput, final TaskResult taskResult) throws FlightServiceException {
        logger.info("TaskInput: " + taskInput.get(inputBookFlight));
        final BookFlightTask bookFlightTask = objectMapper.convertValue(taskInput.get(inputBookFlight),
                BookFlightTask.class);

        FindAndBookFlightInformation flightInformation =
                dtoConverter.convertToFindAndBookFlightInformation(bookFlightTask);
        FlightInformation receivedFlightInformation = flightService.findAndBookFlight(flightInformation);

        BookFlightResponse bookFlightResponse = new BookFlightResponse(bookFlightTask.getTripId(),
                receivedFlightInformation.getId(),
                receivedFlightInformation.getBookingStatus().toString());

        taskResult.getOutputData().put(FlightServiceTasks.TaskOutput.BOOK_FLIGHT_OUTPUT, bookFlightResponse);
        logger.info("Flight successfully booked: " + bookFlightResponse);
    }

}
