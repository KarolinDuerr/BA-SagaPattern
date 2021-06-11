package saga.microprofile.hotelservice.resources;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

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
import java.util.List;
import java.util.logging.Logger;

//@RequestScoped
@ApplicationScoped
@Path("/api/hotels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HotelResource {

    private static final Logger logger = Logger.getLogger(HotelResource.class.toString());

    @Inject
    @HotelServiceImpl
    private IHotelService hotelService;

    @Inject
    private DtoConverter dtoConverter;

    @GET
    @Path("/bookings")
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

//    @LRA(value = LRA.Type.MANDATORY, end = true)
    @LRA(value = LRA.Type.REQUIRED, end = false)
    @POST
    @Path("/bookings")
    public Response bookHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId,
                              @RequestBody final BookHotelRequest bookHotelRequest) {
        logger.info("Received BookHotelRequest: " + bookHotelRequest);

        try {
            HotelBookingInformation bookingInformation =
                    dtoConverter.convertToHotelBookingInformation(bookHotelRequest);
            HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

            BookHotelResponse bookingResponse =
                    new BookHotelResponse(hotelBooking.getBookingInformation().getTripId(), hotelBooking.getId(),
                            hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

            return Response.ok(bookingResponse).build(); // TODO check
        } catch (HotelServiceException exception) {
            logger.warning(exception.toString());
            if (exception.getErrorType() == ErrorType.NO_HOTEL_AVAILABLE) {
                return Response.serverError().entity(new NoHotelAvailable(bookHotelRequest.getTripId())).build();
//                return Response.serverError().build();
            }

            return Response.serverError().entity(new ErrorMessage(exception.getErrorType(), exception.getMessage())).build();
//            return Response.serverError().build();
        }
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response cancelHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        logger.info("Compensate LRA (ID: " + lraId + ")");
//        hotelService.cancelHotelBooking(cancelHotelBooking.getBookingId(), cancelHotelBooking.getTripId());
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response confirmHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        logger.info("Complete LRA (ID: " + lraId + ")");
//        hotelService.confirmHotelBooking(confirmHotelBooking.getBookingId(), confirmHotelBooking.getTripId());
        return Response.ok(ParticipantStatus.Completed).build();
    }

//    @Compensate
//    @Path("bookings/{bookingId}/cancel")
//    @PUT
//    public Response cancelHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final
//    CancelHotelBooking cancelHotelBooking) {
//        logger.info("Compensate LRA (ID: " + lraId + ") --> Cancelling hotel for trip ID: " + cancelHotelBooking
//        .getTripId());
//        hotelService.cancelHotelBooking(cancelHotelBooking.getBookingId(), cancelHotelBooking.getTripId());
//        return Response.ok(ParticipantStatus.Compensated.name()).build();
//    }
//
//    @Complete
//    @Path("bookings/{bookingId}/confirm")
//    @PUT
//    public Response confirmHotel(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId, @RequestBody final
//    ConfirmHotelBooking confirmHotelBooking) {
//        logger.info("Complete LRA (ID: " + lraId + ") --> Confirming the hotel for trip ID: " + confirmHotelBooking
//        .getTripId());
//        hotelService.confirmHotelBooking(confirmHotelBooking.getBookingId(), confirmHotelBooking.getTripId());
//        return Response.ok(ParticipantStatus.Completed).build();
//    }
}
