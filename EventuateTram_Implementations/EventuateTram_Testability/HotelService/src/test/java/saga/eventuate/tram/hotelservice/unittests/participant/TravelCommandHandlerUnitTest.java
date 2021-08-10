package saga.eventuate.tram.hotelservice.unittests.participant;

import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.sagas.testing.SagaParticipantChannels;
import io.eventuate.tram.testutil.TestMessageConsumerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TravelCommandHandlerTestConfiguration.class)
public class TravelCommandHandlerUnitTest {

    @Autowired
    private CommandProducer commandProducer;

    @Autowired
    private TestMessageConsumerFactory testMessageConsumerFactory;

    @Before
    public void setUp() {
//        SagaParticipantStubCommandHandler rejectTripStubCommandHandler = new SagaParticipantStubCommandHandler
//        (TravelServiceChannels.travelServiceChannel, );

        BookHotelRequest bookHotelRequest = Mockito.mock(BookHotelRequest.class);

        SagaParticipantChannels sagaParticipantChannels =
                new SagaParticipantChannels(TravelServiceChannels.travelServiceChannel);
//        MessageConsumer messageConsumer = ;
//        MessageProducer messageProducer = ;
//        SagaParticipantStubManager sagaParticipantStubManager =
//                new SagaParticipantStubManager(sagaParticipantChannels, messageConsumer, messageProducer);
//        sagaParticipantStubManager.forChannel(TravelServiceChannels.travelServiceChannel).when(bookHotelRequest).

    }
// TODO
    @Test
    public void rejectTripCommandShouldRejectTrip() {
//        SagaParticipantStubManager sagaParticipantStubManager = new SagaParticipantStubManager()
    }
}
