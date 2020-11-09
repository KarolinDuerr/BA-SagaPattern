package saga.netflix.conductor.travelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.travelservice.controller.worker.ConfirmTripWorker;
import saga.netflix.conductor.travelservice.controller.worker.RejectTripWorker;
import saga.netflix.conductor.travelservice.resources.DtoConverter;

import java.util.LinkedList;
import java.util.List;

//@Component
public class WorkerDispatcher { // TODO ensure stop polling when instance becomes unhealty --> shutdown() hook in a
    // PreDestroy block

    private static final Logger logger = LoggerFactory.getLogger(WorkerDispatcher.class);

    // At least as many as the number of workers to avoid starvation
    private static final int THREAD_COUNT = 3;

    @Autowired
    private /*final*/ TaskClient taskClient;

    @Autowired
    private /*final*/ MetadataClient metadataClient;

    @Autowired
    private /*final*/ ObjectMapper objectMapper;

    @Autowired
    private /*final*/ ITravelService travelService;

    @Autowired
    private /*final*/ DtoConverter dtoConverter;

    public WorkerDispatcher() {

    }

    public WorkerDispatcher(final TaskClient taskClient, final MetadataClient metadataClient,
                            final ObjectMapper objectMapper, final ITravelService travelService,
                            final DtoConverter dtoConverter) {
        this.taskClient = taskClient;
        this.metadataClient = metadataClient;
        this.objectMapper = objectMapper;
        this.travelService = travelService;
        this.dtoConverter = dtoConverter;
    }

    public void startTaskPolling() {
        final Worker confirmTripWorker = new ConfirmTripWorker(objectMapper, travelService, dtoConverter);
        final Worker rejectTripWorker = new RejectTripWorker(objectMapper, travelService);

        List<Worker> workers = new LinkedList<>();
        workers.add(confirmTripWorker);
        workers.add(rejectTripWorker);

        final TaskRunnerConfigurer taskRunnerConfigurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers).withThreadCount(THREAD_COUNT).build();

        // Start polling for tasks;
        logger.info("Initiated polling for TravelService tasks");
        taskRunnerConfigurer.init();
    }
}
