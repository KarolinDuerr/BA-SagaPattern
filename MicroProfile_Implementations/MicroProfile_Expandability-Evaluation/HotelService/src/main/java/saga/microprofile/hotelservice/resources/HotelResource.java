package saga.microprofile.hotelservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_RECOVERY_HEADER;

import saga.microprofile.hotelservice.api.dto.*;
import saga.microprofile.hotelservice.controller.HotelServiceImpl;
import saga.microprofile.hotelservice.controller.IHotelService;
import saga.microprofile.hotelservice.error.ErrorMessage;
import saga.microprofile.hotelservice.error.ErrorType;
import saga.microprofile.hotelservice.error.HotelServiceException;
import saga.microprofile.hotelservice.model.HotelBooking;
import saga.microprofile.hotelservice.model.HotelBookingInformation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

//@RequestScoped
@ApplicationScoped
@Path("/api/hotels")
public class HotelResource {

    private static final Logger logger = Logger.getLogger(HotelResource.class.toString());

    @Inject
    @HotelServiceImpl
    private IHotelService hotelService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHotelBookings() throws HotelServiceException {
        logger.info("Get hotels.");

        List<HotelBooking> hotelBookings = hotelService.getHotelBookings();

        if (hotelBookings == null) {
            logger.info("Something went wrong during receiving the hotel bookings.");
            throw new WebApplicationException("Something went wrong during receiving the hotel bookings.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToHotelBookingDTOList(hotelBookings)).build();
    }

    @GET
    @Path("/bookings/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHotelBooking(@PathParam(value = "bookingId") final Long bookingId) throws HotelServiceException {
        logger.info("Get hotel with ID: " + bookingId);

        HotelBooking hotelBooking = hotelService.getHotelBooking(bookingId);

        if (hotelBooking == null) {
            logger.info("Something went wrong during receiving the hotel booking.");
            throw new WebApplicationException("Something went wrong during receiving the hotel booking.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToHotelBookingDTO(hotelBooking)).build();
    }

    @LRA(value = LRA.Type.MANDATORY, cancelOn = {Response.Status.INTERNAL_SERVER_ERROR}, cancelOnFamily =
            {Response.Status.Family.CLIENT_ERROR}, end = false)
    @POST
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bookHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                              @RequestBody final BookHotelRequest bookHotelRequest) {
        logger.info("Received BookHotelRequest: " + bookHotelRequest + " with LRA (ID: " + lraId + ")");

        try {
            HotelBookingInformation bookingInformation =
                    dtoConverter.convertToHotelBookingInformation(lraId, bookHotelRequest);
            HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

            BookHotelResponse bookingResponse =
                    new BookHotelResponse(hotelBooking.getBookingInformation().getTripId(), hotelBooking.getId(),
                            hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

            return Response.ok(bookingResponse).build();
        } catch (HotelServiceException exception) {
            logger.warning(exception.toString());
            if (exception.getErrorType() == ErrorType.NO_HOTEL_AVAILABLE) {
                return Response.serverError().entity(new NoHotelAvailable(bookHotelRequest.getTripId())).build();
            }

            return Response.serverError().entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
        }
    }

    @LRA(value = LRA.Type.MANDATORY, end = false)
    @PUT
    @Path("bookings/{bookingId}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final
    ConfirmHotelBooking confirmHotelBooking) {
        logger.info("Confirming the hotel for trip ID: " + confirmHotelBooking.getTripId() + " for LRA (ID: " + lraId + ")");
        hotelService.confirmHotelBooking(confirmHotelBooking.getBookingId(), confirmHotelBooking.getTripId());
        return Response.ok(ParticipantStatus.Completed).build();
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response cancelHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                @HeaderParam(LRA_HTTP_RECOVERY_HEADER) URI recoveryId) {
        logger.info("Compensate LRA (ID: " + lraId + ") with the following recovery ID: " + recoveryId.toString());
        hotelService.cancelHotelBooking(lraId);
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        logger.info("Completing LRA (ID: " + lraId + ")");
        return Response.ok(ParticipantStatus.Completed).build();
    }
}
