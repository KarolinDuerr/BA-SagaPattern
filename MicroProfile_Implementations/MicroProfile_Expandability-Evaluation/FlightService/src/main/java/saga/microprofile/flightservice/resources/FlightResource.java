package saga.microprofile.flightservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import saga.microprofile.flightservice.api.dto.BookFlightRequest;
import saga.microprofile.flightservice.api.dto.BookFlightResponse;
import saga.microprofile.flightservice.api.dto.NoFlightAvailable;
import saga.microprofile.flightservice.controller.FlightServiceImpl;
import saga.microprofile.flightservice.controller.IFlightService;
import saga.microprofile.flightservice.error.ErrorMessage;
import saga.microprofile.flightservice.error.ErrorType;
import saga.microprofile.flightservice.error.FlightServiceException;
import saga.microprofile.flightservice.model.FindAndBookFlightInformation;
import saga.microprofile.flightservice.model.FlightInformation;
import saga.microprofile.flightservice.model.dto.FlightInformationDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_RECOVERY_HEADER;

@ApplicationScoped
@Path("/api/flights")
public class FlightResource {

    private static final Logger logger = Logger.getLogger(FlightResource.class.toString());

    @Inject
    @FlightServiceImpl
    private IFlightService flightService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFlightsInformation() throws FlightServiceException {
        logger.info("Get information about all flights.");

        List<FlightInformation> flightsInformation = flightService.getFlightsInformation();

        if (flightsInformation == null) {
            logger.info("Something went wrong during receiving the flights information.");
            throw new WebApplicationException("Something went wrong during receiving the flights information.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToFlightInformationDTOList(flightsInformation)).build();
    }

    @GET
    @Path("/bookings/{flightBookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFlightInformation(@PathParam(value = "flightBookingId") final Long flightBookingId) throws FlightServiceException {
        logger.info("Get flight booking with ID: " + flightBookingId);

        FlightInformation flightInformation = flightService.getFlightInformation(flightBookingId);

        if (flightInformation == null) {
            logger.info("Something went wrong during receiving the flight information.");
            throw new WebApplicationException("Something went wrong during receiving the flight information.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToFlightInformationDTO(flightInformation)).build();
    }

    @LRA(value = LRA.Type.MANDATORY, cancelOn = {Response.Status.INTERNAL_SERVER_ERROR}, cancelOnFamily =
            {Response.Status.Family.CLIENT_ERROR}, end = false)
    @POST
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bookFlight(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                               @RequestBody final BookFlightRequest bookFlightRequest) {
        logger.info("Received BookFlightRequest: " + bookFlightRequest + " with LRA (ID: " + lraId + ")");

        try {
            FindAndBookFlightInformation flightInformation =
                    dtoConverter.convertToFindAndBookFlightInformation(lraId, bookFlightRequest);
            FlightInformation receivedFlightInformation = flightService.findAndBookFlight(flightInformation);

            BookFlightResponse bookFlightResponse = new BookFlightResponse(receivedFlightInformation.getId(),
                    receivedFlightInformation.getBookingStatus().toString());

            return Response.ok(bookFlightResponse).build();
        } catch (FlightServiceException exception) {
            logger.warning(exception.toString());

            if (exception.getErrorType() == ErrorType.NO_FLIGHT_AVAILABLE) {
                return Response.serverError().entity(new NoFlightAvailable(bookFlightRequest.getTripId())).build();
            }

            return Response.serverError().entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
        }
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response cancelFlight(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) final URI lraId,
                                @HeaderParam(LRA_HTTP_RECOVERY_HEADER) final URI recoveryId) {
        logger.info("Compensate LRA (ID: " + lraId + ") with the following recovery ID: " + recoveryId.toString());
        flightService.cancelFlightBooking(lraId);
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) final URI lraId) {
        logger.info("Completing LRA (ID: " + lraId + ")");
        return Response.ok(ParticipantStatus.Completed).build();
    }
}
