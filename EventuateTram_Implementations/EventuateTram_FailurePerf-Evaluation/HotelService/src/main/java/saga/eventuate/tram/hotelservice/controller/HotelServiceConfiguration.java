package saga.eventuate.tram.hotelservice.controller;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.eventuate.tram.hotelservice.model.HotelBookingRepository;
import saga.eventuate.tram.hotelservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
@Import({SagaParticipantConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class HotelServiceConfiguration {

    @Bean
    public IHotelService hotelService(HotelBookingRepository hotelBookingRepository) {
        return new HotelService(hotelBookingRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Bean
    public HotelCommandHandler hotelCommandHandler(IHotelService hotelService, DtoConverter dtoConverter) {
        return new HotelCommandHandler(hotelService, dtoConverter);
    }

    @Bean
    public CommandDispatcher commandDispatcher(HotelCommandHandler hotelCommandHandler,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("hotelServiceCommandDispatcher",
                hotelCommandHandler.commandHandlers());
    }
}
