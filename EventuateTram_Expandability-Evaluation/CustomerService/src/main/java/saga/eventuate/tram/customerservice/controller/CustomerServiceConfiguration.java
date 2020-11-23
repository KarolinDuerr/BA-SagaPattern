package saga.eventuate.tram.customerservice.controller;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import saga.eventuate.tram.customerservice.model.CustomerRepository;
import saga.eventuate.tram.customerservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
@Import({SagaParticipantConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class CustomerServiceConfiguration {

    @Bean
    public ICustomerService customerService(CustomerRepository customerRepository) {
        CustomerService customerService = new CustomerService(customerRepository);
        customerService.provideExampleEntries();
        return customerService;
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Bean
    public CustomerCommandHandler flightCommandHandler(CustomerService customerService, DtoConverter dtoConverter) {
        return new CustomerCommandHandler(customerService, dtoConverter);
    }

    @Bean
    public CommandDispatcher commandDispatcher(CustomerCommandHandler customerCommandHandler,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("customerServiceCommandDispatcher",
                customerCommandHandler.commandHandlers());
    }
}
