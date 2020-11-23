package saga.netflix.conductor.travelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.utils.JsonMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSaga;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;

@Configuration
public class ConductorConfiguration {

    @Value("${CONDUCTOR_SERVER_URI:conductor.server.uri}")
    String conductorServerUri;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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
        return new JsonMapperProvider().get();
    }

    @Bean
    public BookTripSaga bookTripSaga(RestTemplate restTemplate, ObjectMapper objectMapper) {
        BookTripSaga bookTripSaga = new BookTripSaga(conductorServerUri, restTemplate, objectMapper);
        bookTripSaga.registerWorkflowAndTasks();
        return bookTripSaga;
    }

    @Bean
    public WorkerDispatcher workerDispatcher(TaskClient taskClient, ObjectMapper objectMapper,
                                             ITravelService travelService) {
        WorkerDispatcher createdWorkerDispatcher = new WorkerDispatcher(taskClient, objectMapper, travelService);
        createdWorkerDispatcher.startTaskPolling();
        return createdWorkerDispatcher;
    }
}
