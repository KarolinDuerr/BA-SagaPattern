package saga.eventuate.tram.travelservice.unittests;

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import saga.eventuate.tram.travelservice.controller.ITravelService;
import saga.eventuate.tram.travelservice.controller.TravelService;
import saga.eventuate.tram.travelservice.model.TripInformation;
import saga.eventuate.tram.travelservice.model.TripInformationRepository;
import saga.eventuate.tram.travelservice.saga.BookTripSaga;
import saga.eventuate.tram.travelservice.saga.BookTripSagaData;

import java.util.LinkedList;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
public class TravelServiceUnitTest {

//    @Autowired
    private ITravelService travelService;

//    @MockBean
    private TripInformationRepository tripInformationRepository;

//    @MockBean
    private SagaInstanceFactory sagaInstanceFactory;

//    @MockBean
    private BookTripSaga bookTripSaga;

    private final Long TRIP_INFORMATION_ID = 1L;

    @Before
    public void setup() {
        tripInformationRepository = Mockito.mock(TripInformationRepository.class);
        sagaInstanceFactory = Mockito.mock(SagaInstanceFactory.class);
        bookTripSaga = Mockito.mock(BookTripSaga.class);
        travelService = new TravelService(tripInformationRepository, sagaInstanceFactory, bookTripSaga);
    }

    @Test
    public void bookTripShouldCreateBookTripSaga() {
        // setup
        TripInformation mockedTripInformation = Mockito.mock(TripInformation.class);
        String travellerName = "Test Traveller";

        Mockito.when(mockedTripInformation.getTravellerName()).thenReturn(travellerName);
        Mockito.when(mockedTripInformation.getId()).thenReturn(TRIP_INFORMATION_ID);

        TripInformation mockedTripInfoForList = Mockito.mock(TripInformation.class);
        List<TripInformation> tripInfoList = new LinkedList<>();
        tripInfoList.add(mockedTripInfoForList);
        Mockito.when(tripInformationRepository.findByTravellerName(Mockito.anyString())).thenReturn(tripInfoList);
        Mockito.when(tripInformationRepository.save(Mockito.any(TripInformation.class))).then(saveInvocation -> {
           TripInformation tripInformationSaved = saveInvocation.getArgument(0);
           tripInformationSaved.setId(TRIP_INFORMATION_ID);
           return tripInformationSaved;
        });

        // execute
        travelService.bookTrip(mockedTripInformation);

        // verify
        Mockito.verify(tripInformationRepository).save(Mockito.same(mockedTripInformation));
        Mockito.verify(tripInformationRepository).findByTravellerName(Mockito.same(travellerName));
        Mockito.verify(sagaInstanceFactory).create(Mockito.same(bookTripSaga), Mockito.any(BookTripSagaData.class));
    }
}
