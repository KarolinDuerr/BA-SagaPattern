package saga.eventuate.tram.flightservice.controller;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.eventuate.tram.flightservice.model.FlightInformationRepository;
import saga.eventuate.tram.flightservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
@Import({SagaParticipantConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class FlightServiceConfiguration {

    @Bean
    public IFlightService flightService(FlightInformationRepository flightInformationRepository) {
        return new FlightService(flightInformationRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Bean
    public FlightCommandHandler flightCommandHandler(IFlightService flightService, DtoConverter dtoConverter) {
        return new FlightCommandHandler(flightService, dtoConverter);
    }

    @Bean
    public CommandDispatcher commandDispatcher(FlightCommandHandler flightCommandHandler,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("flightServiceCommandDispatcher",
                flightCommandHandler.commandHandlers());
    }
}
