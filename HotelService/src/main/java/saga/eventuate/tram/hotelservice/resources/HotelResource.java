package saga.eventuate.tram.hotelservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelRequest;
import saga.eventuate.tram.hotelservice.api.dto.BookHotelResponse;
import saga.eventuate.tram.hotelservice.controller.IHotelService;
import saga.eventuate.tram.hotelservice.error.ConverterException;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;

import java.util.List;

@RestController
@RequestMapping(path = "/api/hotels")
public class HotelResource {

    private static final Logger logger = LoggerFactory.getLogger(HotelResource.class);

    @Autowired
    private IHotelService hotelService;

    @GetMapping("/bookings")
    public ResponseEntity<List<HotelBooking>> getHotelBookings() { // TODO DTO
        logger.info("Get hotels.");

        List<HotelBooking> hotelBookings = hotelService.getHotelBookings();
        return ResponseEntity.ok(hotelBookings); // TODO DTO
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<HotelBooking> getHotelBooking(@PathVariable(value = "bookingId") Long bookingId) throws HotelException { // TODO DTO
        logger.info("Get hotel with ID: " + bookingId);

        HotelBooking hotelBooking = hotelService.getHotelBooking(bookingId);
        return ResponseEntity.ok(hotelBooking); // TODO DTO
    }

    @PostMapping
    public ResponseEntity<BookHotelResponse> bookHotel(@RequestBody final BookHotelRequest bookHotelRequest) throws ConverterException {
        logger.info("Book hotel: " + bookHotelRequest);

        if (bookHotelRequest == null) {
            logger.info("BookHotelRequest is missing, therefore no hotel can be booked.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The information to book the hotel is missing.");
        }

        HotelBookingInformation requestedHotelBooking = DtoConverter.instance.convertToHotelBooking(bookHotelRequest);
        HotelBooking receivedHotelBooking = hotelService.bookHotel(requestedHotelBooking);

        if (receivedHotelBooking == null) {
            logger.info("Something went wrong during booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during booking.");
        }

        return ResponseEntity.ok(new BookHotelResponse(receivedHotelBooking.getId(),
                receivedHotelBooking.getHotelName(), receivedHotelBooking.getBookingStatus().toString()));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity cancelHotel(@PathVariable(value = "bookingId") Long bookingId) throws HotelException {
        logger.info("Cancel hotel booking with ID " + bookingId);

        boolean hotelCancelled = hotelService.cancelHotelBooking(bookingId);

        if (!hotelCancelled) {
            logger.info("Something went wrong during cancellation.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "cancellation, hotel could not be cancelled.");
        }

        return ResponseEntity.ok().build();
    }
}
