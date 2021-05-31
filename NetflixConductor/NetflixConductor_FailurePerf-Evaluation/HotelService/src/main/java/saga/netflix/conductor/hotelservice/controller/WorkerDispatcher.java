package saga.netflix.conductor.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.controller.worker.BookHotelWorker;
import saga.netflix.conductor.hotelservice.controller.worker.CancelHotelWorker;
import saga.netflix.conductor.hotelservice.controller.worker.ConfirmHotelWorker;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;

public class WorkerDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(WorkerDispatcher.class);

    // at least as many as the number of workers to avoid starvation
    private static final int THREAD_COUNT = 4;

    @Autowired
    private final TaskClient taskClient;
    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    private TaskRunnerConfigurer taskRunnerConfigurer;

    public WorkerDispatcher(final TaskClient taskClient, final ObjectMapper objectMapper,
                            final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.taskClient = taskClient;
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    public void startTaskPolling(final String conductorServerUri) {
        final Worker bookHotelWorker = new BookHotelWorker(objectMapper, hotelService, dtoConverter,
                conductorServerUri);
        final Worker confirmHotelWorker = new ConfirmHotelWorker(objectMapper, hotelService);
        final Worker cancelHotelWorker = new CancelHotelWorker(objectMapper, hotelService);

        List<Worker> workers = new LinkedList<>();
        workers.add(bookHotelWorker);
        workers.add(confirmHotelWorker);
        workers.add(cancelHotelWorker);

        taskRunnerConfigurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers).withThreadCount(THREAD_COUNT).build();

        // start polling for tasks
        logger.info("Initiated polling for HotelService tasks");
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
