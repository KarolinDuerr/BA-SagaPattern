package saga.netflix.conductor.travelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.utils.JsonMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSaga;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;

@Configuration
@EnableRetry
public class ConductorConfiguration {

    @Value("${CONDUCTOR_SERVER_URI:conductor.server.uri}")
    String conductorServerUri;

    @Bean
    public MetadataClient metaDataClient() {
        MetadataClient metadataClient = new MetadataClient();
        metadataClient.setRootURI(conductorServerUri);
        return metadataClient;
    }

    @Bean
    public WorkflowClient workflowClient() {
        WorkflowClient workflowClient = new WorkflowClient();
        workflowClient.setRootURI(conductorServerUri);
        return workflowClient;
    }

    @Bean
    public TaskClient taskClient() {
        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI(conductorServerUri);
        return taskClient;
    }

    @Bean
    public SagaInstanceFactory sagaInstanceFactory(WorkflowClient workflowClient) {
        return new SagaInstanceFactory(workflowClient);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new JsonMapperProvider().get();
        return objectMapper;
    }

    @Bean
//    @Retryable(value = {SocketException.class, RuntimeException.class, ConductorClientException.class}, maxAttempts = 10, backoff = @Backoff(delay = 20000))
    public BookTripSaga bookTripSaga(MetadataClient metadataClient) {
        try {
            Thread.sleep(60000); // TODO
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BookTripSaga bookTripSaga = new BookTripSaga(metadataClient);
        return bookTripSaga;
    }

    @Bean
    public WorkerDispatcher workerDispatcher(TaskClient taskClient, MetadataClient metadataClient,
                                             ObjectMapper objectMapper, ITravelService travelService) {
        WorkerDispatcher createdWorkerDispatcher = new WorkerDispatcher(taskClient, metadataClient, objectMapper,
                travelService);
        createdWorkerDispatcher.startTaskPolling();
        return createdWorkerDispatcher;
    }
}
