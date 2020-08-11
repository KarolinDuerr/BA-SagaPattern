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
import saga.eventuate.tram.hotelservice.model.dto.HotelBookingDTO;

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
    public ResponseEntity<List<HotelBookingDTO>> getHotelBookings() throws ConverterException {
        logger.info("Get hotels.");

        List<HotelBooking> hotelBookings = hotelService.getHotelBookings();

        if (hotelBookings == null) {
            logger.info("Something went wrong during receiving the hotel bookings.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the hotel bookings.");
        }

        return ResponseEntity.ok(dtoConverter.convertToHotelBookingDTOList(hotelBookings));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<HotelBookingDTO> getHotelBooking(@PathVariable(value = "bookingId") Long bookingId) throws HotelException, ConverterException {
        logger.info("Get hotel with ID: " + bookingId);

        HotelBooking hotelBooking = hotelService.getHotelBooking(bookingId);

        if (hotelBooking == null) {
            logger.info("Something went wrong during receiving the hotel booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during receiving the hotel booking.");
        }

        return ResponseEntity.ok(dtoConverter.convertToHotelBookingDTO(hotelBooking));
    }

    @PostMapping
    public ResponseEntity<BookHotelResponse> bookHotel(@RequestBody final BookHotelRequest bookHotelRequest) throws ConverterException, HotelException {
        logger.info("Book hotel: " + bookHotelRequest);

        if (bookHotelRequest == null) {
            logger.info("BookHotelRequest is missing, therefore no hotel can be booked.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The information to book the hotel is missing.");
        }

        HotelBookingInformation requestedHotelBooking = dtoConverter.convertToHotelBookingInformation(bookHotelRequest);
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
