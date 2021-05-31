package saga.microprofile.hotelservice.resources;

import saga.microprofile.hotelservice.controller.HotelServiceImpl;
import saga.microprofile.hotelservice.controller.IHotelService;
import saga.microprofile.hotelservice.error.HotelServiceException;
import saga.microprofile.hotelservice.model.HotelBooking;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
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
            throw new WebApplicationException("Something went wrong during receiving the hotel bookings.", Response.Status.INTERNAL_SERVER_ERROR);
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
            throw new WebApplicationException("Something went wrong during receiving the hotel booking.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok(dtoConverter.convertToHotelBookingDTO(hotelBooking)).build();
    }
}
