package saga.microprofile.travelservice.resources;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import saga.microprofile.travelservice.api.dto.BookTripRequest;
import saga.microprofile.travelservice.api.dto.BookTripResponse;
import saga.microprofile.travelservice.controller.ITravelService;
import saga.microprofile.travelservice.controller.TravelServiceImpl;
import saga.microprofile.travelservice.error.TravelServiceException;
import saga.microprofile.travelservice.model.TripInformation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
//@Path("/api/travel")
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

    @POST
    public Response bookTrip(@RequestBody final BookTripRequest bookTripRequest) throws TravelServiceException {
        logger.info("Book trip: " + bookTripRequest);

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
}
