package saga.microprofile.travelservice.saga;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import saga.microprofile.hotelservice.api.dto.BookHotelRequest;
import saga.microprofile.hotelservice.api.dto.BookHotelResponse;
import saga.microprofile.hotelservice.api.dto.ConfirmHotelBooking;
import saga.microprofile.hotelservice.api.dto.NoHotelAvailable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
public class BookTripSaga {

    private static final Logger logger = Logger.getLogger(BookTripSaga.class.toString());

//    @Inject
//    @ConfigProperty(name = "hotel.service.uri", defaultValue = "http://localhost:8081/api/hotels/bookings")
    private String hotelServiceURI = "http://localhost:8081/api/hotels/bookings";

//    @Inject
//    @ConfigProperty(name = "flight.service.uri", defaultValue = "http://localhost:8082/api/flights/bookings")
    private String flightServiceURI = "http://localhost:8082/api/flights/bookings";

    private WebTarget hotelServiceTarget;

    private WebTarget flightServiceTarget;

    public BookTripSaga() {
        logger.info("URI's: " + hotelServiceURI + " flight: " + flightServiceURI);
        final Client hotelServiceClient = ClientBuilder.newClient();
        this.hotelServiceTarget = hotelServiceClient.target(hotelServiceURI);
        final Client flightServiceClient = ClientBuilder.newClient();
        this.flightServiceTarget = flightServiceClient.target(flightServiceURI);
    }

    public void startBookTripSaga(BookTripSagaData bookTripSagaData) {
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        logger.info("BookHotelRequest: " + bookHotelRequest.toString());
        Response hotelResponse = hotelServiceTarget.request().post(Entity.entity(bookHotelRequest,
                MediaType.APPLICATION_JSON_TYPE));
        logger.info("HotelResponse: " + hotelResponse);
        logger.info("HotelResponse StatusInfo Int: " + hotelResponse.getStatus());
        logger.info("HotelResponse Test: " + hotelResponse.readEntity(BookHotelResponse.class));
        logger.info("HotelResponse Test2: " + hotelResponse.readEntity(NoHotelAvailable.class));

    }
}
