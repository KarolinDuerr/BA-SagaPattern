package saga.netflix.conductor.travelservice.saga.configuration;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.workflow.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;

import java.util.HashMap;
import java.util.Map;

public class BookTripTasksFactory { // TODO: choose option: eigene Worker und WorkerTasks oder SystemTasks (Kafka
    // oder HTTP)

    @Autowired
    private final MetadataClient metadataClient; // TODO: keep here?

    public BookTripTasksFactory(final MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
        BookTripTasksDefinitionFactory bookTripTasksDefinitionFactory =
                new BookTripTasksDefinitionFactory(this.metadataClient);
        bookTripTasksDefinitionFactory.registerTaskDefinitions();
    }

    public WorkflowTask bookHotelTask() {
        WorkflowTask bookHotelTask = new WorkflowTask();
        bookHotelTask.setName(HotelServiceTasks.Task.BOOK_HOTEL);
        bookHotelTask.setTaskReferenceName(HotelServiceTasks.Task.BOOK_HOTEL);
        bookHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, String.format("${workflow.input.%s}",
                HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
        bookHotelTask.setInputParameters(input);

        return bookHotelTask;
    }

    public WorkflowTask cancelHotelTask() {
        WorkflowTask cancelHotelTask = new WorkflowTask();
        cancelHotelTask.setName(HotelServiceTasks.Task.CANCEL_HOTEL);
        cancelHotelTask.setTaskReferenceName(HotelServiceTasks.Task.CANCEL_HOTEL);
        cancelHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT, String.format("${workflow.input.%s}", HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
        cancelHotelTask.setInputParameters(input);

        return cancelHotelTask;
    }

    public WorkflowTask bookFlightTask() {
        WorkflowTask bookFlightTask = new WorkflowTask();
        bookFlightTask.setName(FlightServiceTasks.Task.BOOK_FLIGHT);
        bookFlightTask.setTaskReferenceName(FlightServiceTasks.Task.BOOK_FLIGHT);
        bookFlightTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT, String.format("${workflow.input.%s}",
                FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT));
        bookFlightTask.setInputParameters(input);

        return bookFlightTask;
    }

    public WorkflowTask cancelFlightTask() {
        WorkflowTask cancelHotelTask = new WorkflowTask();
        cancelHotelTask.setName(FlightServiceTasks.Task.CANCEL_FLIGHT);
        cancelHotelTask.setTaskReferenceName(FlightServiceTasks.Task.CANCEL_FLIGHT);
        cancelHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT, String.format("${workflow.input.%s}", FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT));
        cancelHotelTask.setInputParameters(input);

        return cancelHotelTask;
    }

    public WorkflowTask confirmHotelTask() {
        WorkflowTask confirmHotelTask = new WorkflowTask();
        confirmHotelTask.setName(HotelServiceTasks.Task.CONFIRM_HOTEL);
        confirmHotelTask.setTaskReferenceName(HotelServiceTasks.Task.CONFIRM_HOTEL);
        confirmHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT, String.format("${%s.output.%s}",
                HotelServiceTasks.Task.BOOK_HOTEL, HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT));
        confirmHotelTask.setInputParameters(input);

        return confirmHotelTask;
    }

    public WorkflowTask confirmTripTask() {
        WorkflowTask confirmTripTask = new WorkflowTask();
        confirmTripTask.setName(TravelServiceTasks.Task.CONFIRM_TRIP);
        confirmTripTask.setTaskReferenceName(TravelServiceTasks.Task.CONFIRM_TRIP);
        confirmTripTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT, String.format("${%s.output.%s}",
                HotelServiceTasks.Task.BOOK_HOTEL, HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT));
        input.put(TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT, String.format("${%s.output.%s}",
                FlightServiceTasks.Task.BOOK_FLIGHT, FlightServiceTasks.TaskOutput.BOOK_FLIGHT_OUTPUT));
        confirmTripTask.setInputParameters(input);

        return confirmTripTask;
    }

    public WorkflowTask rejectTripTask() {
        WorkflowTask cancelHotelTask = new WorkflowTask();
        cancelHotelTask.setName(TravelServiceTasks.Task.REJECT_TRIP);
        cancelHotelTask.setTaskReferenceName(TravelServiceTasks.Task.REJECT_TRIP);
        cancelHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT, String.format("${workflow.input.%s}", TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT));
        input.put(TravelServiceTasks.TaskInput.REJECT_TRIP_ID_INPUT, String.format("${workflow.input.%s}", HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
        cancelHotelTask.setInputParameters(input);

        return cancelHotelTask;
    }
}
