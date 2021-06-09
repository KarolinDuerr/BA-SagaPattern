package saga.microprofile.travelservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

import saga.microprofile.travelservice.api.dto.BookTripRequest;
import saga.microprofile.travelservice.api.dto.BookTripResponse;
import saga.microprofile.travelservice.api.dto.ConfirmTripBooking;
import saga.microprofile.travelservice.api.dto.RejectTripRequest;
import saga.microprofile.travelservice.controller.ITravelService;
import saga.microprofile.travelservice.controller.TravelServiceImpl;
import saga.microprofile.travelservice.error.TravelServiceException;
import saga.microprofile.travelservice.model.TripInformation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Path("api/travel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TravelResource {

    private static final Logger logger = Logger.getLogger(TravelResource.class.toString());

    @Inject
    @TravelServiceImpl
    private ITravelService travelService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/trips")
    public Response getTrips() throws TravelServiceException {
        logger.info("Get trips.");
        logger.info("Travel Service: " + travelService);

        List<TripInformation> trips = travelService.getTripsInformation();

        if (trips == null) {
            logger.info("Something went wrong during receiving the trips information.");
            throw new WebApplicationException("Something went wrong during receiving the trips information.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToTripInformationDTOList(trips)).build(); // TODO check
    }

    @GET
    @Path("trips/{tripId}")
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

//    @LRA(value = LRA.Type.REQUIRES_NEW)
//    @POST
//    public Response bookTrip(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final BookTripRequest bookTripRequest) throws TravelServiceException {
//        logger.info("Book trip: " + bookTripRequest + "with LRA ID: " + lraId); // TODO save lraId somewhere=
//
//        if (bookTripRequest == null) {
//            logger.info("BookTripRequest is missing, therefore no trip can be booked.");
//            throw new WebApplicationException("The information to book the trip is missing.",
//                    Response.Status.BAD_REQUEST);
//        }
//
//        TripInformation tripInformation =
//                travelService.bookTrip(dtoConverter.convertToTripInformation(bookTripRequest));
//
//        if (tripInformation == null) {
//            logger.info("Something went wrong during booking.");
//            throw new WebApplicationException("Something went wrong during booking.", Response.Status.INTERNAL_SERVER_ERROR);
//        }
//
////        return Response.created(UriBuilder.fromResource(this.getClass()).path(Long.toString(tripInformation.getId())).build()).build();
//        return Response.ok(new BookTripResponse(tripInformation.getId(),
//                tripInformation.getBookingStatus().toString())).build(); //TODO
//    }

    @LRA(value = LRA.Type.REQUIRES_NEW)
    @POST
    @Path("/book")
    public Response bookTrip(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final BookTripRequest bookTripRequest) throws TravelServiceException {
        logger.info("Book trip: " + bookTripRequest + "with LRA ID: " + lraId); // TODO save lraId somewhere=

        if (bookTripRequest == null) {
            logger.info("BookTripRequest is missing, therefore no trip can be booked.");
            throw new WebApplicationException("The information to book the trip is missing.",
                    Response.Status.BAD_REQUEST);
        }

        TripInformation tripInformation =
                travelService.bookTrip(dtoConverter.convertToTripInformation(bookTripRequest));

        if (tripInformation == null) {
            logger.info("Something went wrong during booking.");
            throw new WebApplicationException("Something went wrong during booking.", Response.Status.INTERNAL_SERVER_ERROR);
        }

//        return Response.created(UriBuilder.fromResource(this.getClass()).path(Long.toString(tripInformation.getId())).build()).build();
        return Response.ok(new BookTripResponse(tripInformation.getId(),
                tripInformation.getBookingStatus().toString())).build(); //TODO
    }

    @Compensate
    @Path("/compensate")
    @PUT
//    public Response rejectTripBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @PathParam(value = "tripId") final Long tripId, @RequestBody final RejectTripRequest rejectTripRequest) {
    public Response rejectTripBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId) {
//        logger.info("Compensate LRA (ID: " + lraId + ") --> Rejecting trip with ID: " + tripId);
        logger.info("Compensate LRA (ID: " + lraId + ")");
//        travelService.rejectTrip(tripId, rejectTripRequest.getRejectionReason());
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete // TODO anders --> hier nicht mehr aktiv machen lediglich resourcen freigeben
    @Path("/complete")
    @PUT
    public Response confirmTripBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final ConfirmTripBooking confirmTripBooking) {
        logger.info("Completing LRA (ID: " + lraId + ") --> Confirming trip with ID: " + confirmTripBooking.getTripId());
        travelService.confirmTripBooking(confirmTripBooking.getTripId(), confirmTripBooking.getHotelId(), confirmTripBooking.getFlightId());
        return Response.ok(ParticipantStatus.Completed).build();
    }

//    @Compensate
//    @Path("trips/{tripId}/reject")
//    @PUT
//    public Response rejectTripBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @PathParam(value = "tripId") final Long tripId, @RequestBody final RejectTripRequest rejectTripRequest) {
//        logger.info("Compensate LRA (ID: " + lraId + ") --> Rejecting trip with ID: " + tripId);
//        travelService.rejectTrip(tripId, rejectTripRequest.getRejectionReason());
//        return Response.ok(ParticipantStatus.Compensated.name()).build();
//    }
//
//    @Complete // TODO anders --> hier nicht mehr aktiv machen lediglich resourcen freigeben
//    @Path("trips/{tripId}/confirm")
//    @PUT
//    public Response confirmTripBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @PathParam(value = "tripId") final Long tripId, @RequestBody final ConfirmTripBooking confirmTripBooking) {
//        logger.info("Completing LRA (ID: " + lraId + ") --> Confirming trip with ID: " + tripId);
//        travelService.confirmTripBooking(tripId, confirmTripBooking.getHotelId(), confirmTripBooking.getFlightId());
//        return Response.ok(ParticipantStatus.Completed).build();
//    }

    @LRA(value = LRA.Type.REQUIRES_NEW)
    @POST
    @Path("test")
    public Response test(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, String test) throws TravelServiceException {
        logger.info("String: " + test + "with LRA ID: " + lraId); // TODO save lraId somewhere=
        return Response.ok(test).build(); //TODO
    }
}
