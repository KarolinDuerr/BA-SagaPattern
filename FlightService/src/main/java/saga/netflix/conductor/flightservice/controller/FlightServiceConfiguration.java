package saga.netflix.conductor.flightservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.common.utils.JsonMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.netflix.conductor.flightservice.model.FlightInformationRepository;
import saga.netflix.conductor.flightservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class FlightServiceConfiguration {


    @Value("${conductor.server.uri}")
    String conductorServerUri;

    @Bean
    public IFlightService flightService(FlightInformationRepository flightInformationRepository) {
        return new FlightService(flightInformationRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

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
    public MetadataClient metaDataClient() {
        MetadataClient metadataClient = new MetadataClient();
        metadataClient.setRootURI(conductorServerUri);
        return metadataClient;
    }

    @Bean
//    @Retryable(value = {SocketException.class, RuntimeException.class, ConductorClientException.class}, maxAttempts = 10, backoff = @Backoff(delay = 20000))
    public WorkerDispatcher workerDispatcher(TaskClient taskClient, MetadataClient metadataClient,
                                             ObjectMapper objectMapper, IFlightService flightService,
                                             DtoConverter dtoConverter) {
        try {
            Thread.sleep(65000); // TODO
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WorkerDispatcher createdWorkerDispatcher = new WorkerDispatcher(taskClient, metadataClient, objectMapper,
                flightService, dtoConverter);
        createdWorkerDispatcher.startTaskPolling();
        return createdWorkerDispatcher;
    }
}
