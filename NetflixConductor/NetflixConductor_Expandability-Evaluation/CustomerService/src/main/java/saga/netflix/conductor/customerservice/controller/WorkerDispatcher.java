package saga.netflix.conductor.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;

public class WorkerDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(WorkerDispatcher.class);

    // At least as many as the number of workers to avoid starvation
    private static final int THREAD_COUNT = 2;

    @Autowired
    private final TaskClient taskClient;

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ICustomerService customerService;

    private TaskRunnerConfigurer taskRunnerConfigurer;

    public WorkerDispatcher(final TaskClient taskClient, final ObjectMapper objectMapper,
                            final ICustomerService customerService) {
        this.taskClient = taskClient;
        this.objectMapper = objectMapper;
        this.customerService = customerService;
    }

    public void startTaskPolling() {
        final Worker validateCustomerWorker = new ValidateCustomerWorker(objectMapper, customerService);

        List<Worker> workers = new LinkedList<>();
        workers.add(validateCustomerWorker);

        taskRunnerConfigurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers).withThreadCount(THREAD_COUNT).build();

        // Start polling for tasks;
        logger.info("Initiated polling for FlightService tasks");
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
