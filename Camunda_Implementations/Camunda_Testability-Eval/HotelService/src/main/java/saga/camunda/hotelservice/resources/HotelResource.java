package saga.camunda.hotelservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.hotelservice.model.HotelBooking;
import saga.camunda.hotelservice.error.HotelServiceException;
import saga.camunda.hotelservice.model.dto.HotelBookingDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/hotels")
public class HotelResource {

    private static final Logger logger = LoggerFactory.getLogger(HotelResource.class);

    @Autowired
    private IHotelService hotelService;

    @Autowired
    private DtoConverter dtoConverter;

    @GetMapping("/bookings")
    public ResponseEntity<List<HotelBookingDTO>> getHotelBookings() throws HotelServiceException {
        logger.info("Get hotels.");

        List<HotelBooking> hotelBookings = hotelService.getHotelBookings();

        if (hotelBookings == null) {
            logger.info("Something went wrong during receiving the hotel bookings.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the hotel bookings.");
        }

        return ResponseEntity.ok(dtoConverter.convertToHotelBookingDTOList(hotelBookings));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<HotelBookingDTO> getHotelBooking(@PathVariable(value = "bookingId") final Long bookingId) throws HotelServiceException {
        logger.info("Get hotel with ID: " + bookingId);

        HotelBooking hotelBooking = hotelService.getHotelBooking(bookingId);

        if (hotelBooking == null) {
            logger.info("Something went wrong during receiving the hotel booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the hotel booking.");
        }

        return ResponseEntity.ok(dtoConverter.convertToHotelBookingDTO(hotelBooking));
    }
}
