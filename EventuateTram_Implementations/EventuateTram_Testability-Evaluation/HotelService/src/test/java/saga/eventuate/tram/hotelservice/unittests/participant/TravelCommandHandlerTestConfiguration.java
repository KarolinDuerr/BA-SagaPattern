package saga.eventuate.tram.hotelservice.unittests.participant;

import io.eventuate.tram.inmemory.TramInMemoryConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.testutil.TestMessageConsumerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({
//        AccountingMessagingConfiguration.class,
        TramCommandProducerConfiguration.class,
//        EmbeddedTestAggregateStoreConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramInMemoryConfiguration.class})
public class TravelCommandHandlerTestConfiguration {

    @Bean
    public TestMessageConsumerFactory testMessageConsumerFactory() {
        return new TestMessageConsumerFactory();
    }
}
