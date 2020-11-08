package saga.netflix.conductor.travelservice.saga.configuration;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.workflow.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;

import java.util.HashMap;
import java.util.Map;

public class BookTripTasksFactory { // TODO: choose option: eigene Worker und WorkerTasks oder SystemTasks (Kafka oder HTTP)

    @Autowired
    private final MetadataClient metadataClient; // TODO: keep here?

    public BookTripTasksFactory(final MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
        BookTripTasksDefinitionFactory bookTripTasksDefinitionFactory = new BookTripTasksDefinitionFactory(this.metadataClient);
        bookTripTasksDefinitionFactory.registerTaskDefinitions();
    }

    public WorkflowTask bookHotelTask() {
        WorkflowTask bookHotelTask = new WorkflowTask();
        bookHotelTask.setName(HotelServiceTasks.Task.BOOK_HOTEL);
        bookHotelTask.setTaskReferenceName(HotelServiceTasks.Task.BOOK_HOTEL);
        bookHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, String.format("$workflow.input.%s", HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT));
        bookHotelTask.setInputParameters(input);

        return bookHotelTask;
    }

    public WorkflowTask cancelHotelTask() {
        WorkflowTask cancelHotelTask = new WorkflowTask();
        cancelHotelTask.setName(HotelServiceTasks.Task.CANCEL_HOTEL);
        cancelHotelTask.setTaskReferenceName(HotelServiceTasks.Task.CANCEL_HOTEL);
        cancelHotelTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT, String.format("$workflow.input.%s", HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT)); // TODO check
        cancelHotelTask.setInputParameters(input);

        return cancelHotelTask;
    }

    public WorkflowTask rejectTripTask() {
        WorkflowTask rejectTripTask = new WorkflowTask();
        rejectTripTask.setName(TravelServiceTasks.Task.REJECT_TRIP);
        rejectTripTask.setTaskReferenceName(TravelServiceTasks.Task.REJECT_TRIP);
        rejectTripTask.setWorkflowTaskType(TaskType.SIMPLE); // TODO: choose option

        Map<String, Object> input = new HashMap<>();
        input.put(TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT, String.format("$workflow.input.%s", TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT)); // TODO check
        rejectTripTask.setInputParameters(input);

        return rejectTripTask;
    }

    //TODO
}
