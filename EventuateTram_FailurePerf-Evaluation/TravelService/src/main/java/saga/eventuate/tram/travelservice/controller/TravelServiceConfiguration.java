package saga.eventuate.tram.travelservice.controller;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.eventuate.tram.travelservice.model.TripInformationRepository;
import saga.eventuate.tram.travelservice.resources.DtoConverter;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;

@Configuration
@EnableAutoConfiguration
@Import({SagaOrchestratorConfiguration.class, SagaParticipantConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class TravelServiceConfiguration {

    @Bean
    public ITravelService travelService(TripInformationRepository tripInformationRepository, SagaInstanceFactory sagaInstanceFactory, BookTripSaga bookTripSaga) {
        return new TravelService(tripInformationRepository, sagaInstanceFactory, bookTripSaga);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Bean
    public BookTripSaga bookTripSaga() {
        return new BookTripSaga();
    }

    @Bean
    public TravelCommandHandler travelCommandHandler(ITravelService travelService) {
        return new TravelCommandHandler(travelService);
    }

    @Bean
    public CommandDispatcher commandDispatcher(TravelCommandHandler travelCommandHandler,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("travelServiceCommandDispatcher",
                travelCommandHandler.commandHandlers());
    }
}
