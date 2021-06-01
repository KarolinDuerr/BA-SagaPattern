package saga.eventuate.tram.eventservice.controller;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.eventuate.tram.eventservice.model.EventBookingRepository;
import saga.eventuate.tram.eventservice.model.EventRepository;
import saga.eventuate.tram.eventservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
@Import({SagaParticipantConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class EventServiceConfiguration {

    @Bean
    public IEventService eventService(final EventBookingRepository eventBookingRepository,
                                      final EventRepository eventRepository) {
        EventService eventService = new EventService(eventBookingRepository, eventRepository);
        eventService.provideExampleEntries();
        return eventService;
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Bean
    public EventCommandHandler flightCommandHandler(final IEventService eventService) {
        return new EventCommandHandler(eventService);
    }

    @Bean
    public CommandDispatcher commandDispatcher(final EventCommandHandler eventCommandHandler,
                                               final SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("eventServiceCommandDispatcher",
                eventCommandHandler.commandHandlers());
    }
}
