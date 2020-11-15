package saga.netflix.conductor.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.common.utils.JsonMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

@Configuration
//@EnableRetry
public class ConductorConfiguration {

    @Value("${conductor.server.uri}")
    String conductorServerUri;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new JsonMapperProvider().get();
        return objectMapper;
    }

    @Bean
    public TaskClient taskClient() {
        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI(conductorServerUri);
        return taskClient;
    }

    @Bean
//    @Retryable(value = {SocketException.class, RuntimeException.class, ConductorClientException.class}, maxAttempts
//    = 10, backoff = @Backoff(delay = 20000))
    public WorkerDispatcher workerDispatcher(TaskClient taskClient, ObjectMapper objectMapper,
                                             IHotelService hotelService, DtoConverter dtoConverter) {
        try {
            Thread.sleep(50000); // TODO
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WorkerDispatcher createdWorkerDispatcher = new WorkerDispatcher(taskClient, objectMapper, hotelService,
                dtoConverter);
        createdWorkerDispatcher.startTaskPolling();
        return createdWorkerDispatcher;
    }
}
