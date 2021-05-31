package saga.netflix.conductor.travelservice.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.controller.ITravelService;

public class ConfirmTripCancellationWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmTripCancellationWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ITravelService travelService;

    private final String inputHotelConfirmation = TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT; // TODO

    private final String inputFlightConfirmation = TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT; // TODO

    public ConfirmTripCancellationWorker(final ObjectMapper objectMapper, final ITravelService travelService) {
        this.objectMapper = objectMapper;
        this.travelService = travelService;
    }

    @Override
    public String getTaskDefName() {
        return TravelServiceTasks.Task.CONFIRM_TRIP_CANCELLATION;
    }

    @Override
    public TaskResult execute(final Task task) {
        // TODO
        return null;
    }
}
