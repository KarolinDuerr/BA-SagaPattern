package saga.netflix.conductor.travelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.travelservice.controller.worker.ConfirmTripWorker;
import saga.netflix.conductor.travelservice.controller.worker.RejectTripWorker;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;

public class WorkerDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(WorkerDispatcher.class);

    // at least as many as the number of workers to avoid starvation
    private static final int THREAD_COUNT = 3;

    @Autowired
    private final TaskClient taskClient;

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ITravelService travelService;

    private TaskRunnerConfigurer taskRunnerConfigurer;

    public WorkerDispatcher(final TaskClient taskClient, final ObjectMapper objectMapper,
                            final ITravelService travelService) {
        this.taskClient = taskClient;
        this.objectMapper = objectMapper;
        this.travelService = travelService;
    }

    public void startTaskPolling() {
        final Worker confirmTripWorker = new ConfirmTripWorker(objectMapper, travelService);
        final Worker rejectTripWorker = new RejectTripWorker(objectMapper, travelService);

        List<Worker> workers = new LinkedList<>();
        workers.add(confirmTripWorker);
        workers.add(rejectTripWorker);

        taskRunnerConfigurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers).withThreadCount(THREAD_COUNT).build();

        // start polling for tasks;
        logger.info("Initiated polling for TravelService tasks");
        taskRunnerConfigurer.init();
    }

    @PreDestroy
    private void shutdownWorkers() {
        logger.info("Shutdown --> stop polling");
        if (taskRunnerConfigurer != null) {
            this.taskRunnerConfigurer.shutdown();
        }
    }
}
