package saga.microprofile.travelservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import saga.microprofile.travelservice.api.dto.BookTripRequest;
import saga.microprofile.travelservice.api.dto.BookTripResponse;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.controller.ITravelService;
import saga.microprofile.travelservice.controller.TravelServiceImpl;
import saga.microprofile.travelservice.error.TravelServiceException;
import saga.microprofile.travelservice.model.TripInformation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("api/travel")
@Tag(name = "TravelResource", description = "An example for a Travel Service that enables the user to book trips and " +
        "to see already booked trips.")
@Server(url = "http://localhost:8090/", description = "The server for the TravelResource endpoint.")
public class TravelResource {

    private static final Logger logger = Logger.getLogger(TravelResource.class.toString());

    @Inject
    @TravelServiceImpl
    private ITravelService travelService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/trips")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrips() throws TravelServiceException {
        logger.info("Get trips.");

        List<TripInformation> trips = travelService.getTripsInformation();

        if (trips == null) {
            logger.info("Something went wrong during receiving the trips information.");
            throw new WebApplicationException("Something went wrong during receiving the trips information.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToTripInformationDTOList(trips)).build();
    }

    @GET
    @Path("trips/{tripId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrip(@PathParam(value = "tripId") final Long tripId) throws TravelServiceException {
        logger.info("Get trip with ID: " + tripId);

        TripInformation tripInformation = travelService.getTripInformation(tripId);

        if (tripInformation == null) {
            logger.info("Something went wrong during receiving the trip information.");
            throw new WebApplicationException("Something went wrong during receiving the trip information.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToTripInformationDTO(tripInformation)).build();
    }

    @LRA(value = LRA.Type.REQUIRES_NEW, cancelOn = {Response.Status.INTERNAL_SERVER_ERROR}, cancelOnFamily =
            {Response.Status.Family.CLIENT_ERROR}, end = false)
    @POST
    @Path("/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bookTrip(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                             @RequestBody final BookTripRequest bookTripRequest) throws TravelServiceException {
        logger.info("Book trip: " + bookTripRequest + " with LRA ID: " + lraId);

        if (bookTripRequest == null) {
            logger.info("BookTripRequest is missing, therefore no trip can be booked.");
            throw new WebApplicationException("The information to book the trip is missing.",
                    Response.Status.BAD_REQUEST);
        }

        TripInformation tripInformation =
                travelService.bookTrip(dtoConverter.convertToTripInformation(lraId, bookTripRequest));

        if (tripInformation == null) {
            logger.info("Something went wrong during booking.");
            throw new WebApplicationException("Something went wrong during booking.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(new BookTripResponse(tripInformation.getId(),
                tripInformation.getBookingStatus().toString())).build();
    }

    @LRA(value = LRA.Type.MANDATORY,  cancelOn = {Response.Status.INTERNAL_SERVER_ERROR}, cancelOnFamily =
            {Response.Status.Family.CLIENT_ERROR}, end = true)
    @Path("trips/{tripId}/confirm")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmTrip(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                       @PathParam(value = "tripId") final Long tripId,
                                       @RequestBody final ConfirmTripBooking confirmTripBooking) {
        logger.info("Confirming trip with ID: " + tripId + " for LRA (ID: " + lraId + ")");
        travelService.confirmTripBooking(tripId, confirmTripBooking.getHotelId(), confirmTripBooking.getFlightId());
        return Response.ok().build();
    }

    @Compensate
    @Path("/compensate")
    @PUT
    @Operation(summary = "Compensate method for a failed LRA. Don't invoke from the outside.", description = "The " +
            "compensate method for this resource that the LRA Coordinator invokes when an LRA has failed and to " +
            "inform the participants to compensate for their performed actions.")
    public Response rejectTrip(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        logger.info("Compensate LRA (ID: " + lraId + ") --> Reject trip with related ID.");
        travelService.rejectTrip(lraId);
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete
    @Path("/complete")
    @PUT
    @Operation(summary = "Complete method for a finished LRA. Don't invoke from the outside.", description = "The " +
            "complete method of this resource that the LRA Coordinator invokes when an LRA has successfully finished " +
            "and it wants to close it.")
    public Response completeTripBookingSaga(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        logger.info("Completing LRA (ID: " + lraId + ")");
        return Response.ok(ParticipantStatus.Completed).build();
    }
}
