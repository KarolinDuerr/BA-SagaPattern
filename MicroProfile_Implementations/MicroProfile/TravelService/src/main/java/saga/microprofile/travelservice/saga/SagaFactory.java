package saga.microprofile.travelservice.saga;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import saga.microprofile.travelservice.controller.ITravelService;
import saga.microprofile.travelservice.controller.TravelServiceImpl;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

@ApplicationScoped
public class SagaFactory {

    private static final Logger logger = Logger.getLogger(SagaFactory.class.toString());

    @Resource(lookup = "concurrent/threadFactory2")
    private ThreadFactory threadFactory;

    @Inject
//    @ConfigProperty(name = "hotel.service.uri", defaultValue = "http://localhost:8081/api/hotels/bookings")
//    private String hotelServiceURI;
    private String hotelServiceURI = "http://localhost:8081/api/hotels/bookings";

    @Inject
//    @ConfigProperty(name = "flight.service.uri", defaultValue = "http://localhost:8082/api/flights/bookings")
//    private String flightServiceURI;
    private String flightServiceURI = "http://localhost:8082/api/flights/bookings";

    private String travelServiceURI = "http://localhost:8090/api/travel";

    @Inject
    @TravelServiceImpl
    private ITravelService travelService;

    public SagaFactory() {
        logger.info("URI's: " + hotelServiceURI + " flight: " + flightServiceURI);
    }

    public void startBookTripSaga(BookTripSagaData bookTripSagaData) {
        logger.info("Starting BookTripSaga");
        threadFactory.newThread(new BookTripSaga(bookTripSagaData, flightServiceURI, hotelServiceURI,
                travelServiceURI)).start();
    }
}
