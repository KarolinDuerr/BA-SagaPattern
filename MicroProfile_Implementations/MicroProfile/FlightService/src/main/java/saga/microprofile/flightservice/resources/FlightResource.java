package saga.microprofile.flightservice.resources;

import saga.microprofile.flightservice.controller.FlightServiceImpl;
import saga.microprofile.flightservice.controller.IFlightService;
import saga.microprofile.flightservice.error.FlightServiceException;
import saga.microprofile.flightservice.model.FlightInformation;
import saga.microprofile.flightservice.model.dto.FlightInformationDTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Path("/api/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    private static final Logger logger = Logger.getLogger(FlightResource.class.toString());

    @Inject
    @FlightServiceImpl
    private IFlightService flightService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/bookings")
    public Response getFlightsInformation() throws FlightServiceException {
        logger.info("Get information about all flights.");

        List<FlightInformation> flightsInformation = flightService.getFlightsInformation();

        if (flightsInformation == null) {
            logger.info("Something went wrong during receiving the flights information.");
            throw new WebApplicationException("Something went wrong during receiving the flights information.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToFlightInformationDTOList(flightsInformation)).build();
    }

    @GET
    @Path("/bookings/{flightBookingId}")
    public Response getFlightInformation(@PathParam(value = "flightBookingId") final Long flightBookingId) throws FlightServiceException {
        logger.info("Get flight booking with ID: " + flightBookingId);

        FlightInformation flightInformation = flightService.getFlightInformation(flightBookingId);

        if (flightInformation == null) {
            logger.info("Something went wrong during receiving the flight information.");
            throw new WebApplicationException("Something went wrong during receiving the flight information.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToFlightInformationDTO(flightInformation)).build();
    }
}
