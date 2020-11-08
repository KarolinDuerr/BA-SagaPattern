package saga.netflix.conductor.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

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
    private /*final*/ IHotelService hotelService;

    @Autowired
    private /*final*/ DtoConverter dtoConverter;

    public WorkerDispatcher() {

    }

    public WorkerDispatcher(final TaskClient taskClient, final MetadataClient metadataClient,
                            final ObjectMapper objectMapper, final IHotelService hotelService,
                            final DtoConverter dtoConverter) {
        this.taskClient = taskClient;
        this.metadataClient = metadataClient;
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    public void startTaskPolling() {
        final Worker bookHotelWorker = new BookHotelWorker(objectMapper, hotelService, dtoConverter);

        List<Worker> workers = new LinkedList<>();
        workers.add(bookHotelWorker);

        final TaskRunnerConfigurer taskRunnerConfigurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers).withThreadCount(THREAD_COUNT).build();

        // Start polling for tasks;
        logger.info("Initiated polling for HotelService tasks");
        taskRunnerConfigurer.init();
    }
}
