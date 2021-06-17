package saga.microprofile.travelservice.saga;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
    @ConfigProperty(name = "hotel.service.uri", defaultValue = "http://localhost:8081/api/hotels/bookings")
    private String hotelServiceBaseUri;

    @Inject
    @ConfigProperty(name = "flight.service.uri", defaultValue = "http://localhost:8082/api/flights/bookings")
    private String flightServiceBaseUri;

    @Inject
    @ConfigProperty(name = "travel.service.uri", defaultValue = "http://localhost:8090/api/travel")
    private String travelServiceBaseUri;

    public void startBookTripSaga(final BookTripSagaData bookTripSagaData, final String lraId) {
        logger.info("Starting BookTripSaga");
        threadFactory.newThread(new BookTripSaga(bookTripSagaData, flightServiceBaseUri, hotelServiceBaseUri,
                travelServiceBaseUri, lraId)).start();
    }
}
