package saga.microprofile.travelservice.integrationtests;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import saga.microprofile.travelservice.error.TravelServiceException;
import saga.microprofile.travelservice.model.dto.TripInformationDTO;
import saga.microprofile.travelservice.resources.TravelResource;

import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@MicroShedTest
@SharedContainerConfig(AppDeploymentConfig.class)
public class BookTripSagaIT {

    @RESTClient
    public static TravelResource travelResource;

    @Test
    public void successfulHotelBookingForBookHotelEndpointShouldReturnOk() throws TravelServiceException {
        // setup
        Response expected = Response.ok("Ready").build();

        // execute
        Response response = travelResource.healthEndpointForTests();

        // verify
        Assert.assertEquals(expected, response);
    }
}
