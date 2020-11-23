package saga.netflix.conductor.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.common.utils.JsonMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConductorConfiguration {

    @Value("${conductor.server.uri}")
    String conductorServerUri;

    @Bean
    public ObjectMapper objectMapper() {
        return new JsonMapperProvider().get();
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
                                             ICustomerService customerService) {
        try {
            Thread.sleep(65000); // TODO
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WorkerDispatcher createdWorkerDispatcher = new WorkerDispatcher(taskClient, objectMapper, customerService);
        createdWorkerDispatcher.startTaskPolling();
        return createdWorkerDispatcher;
    }

}
