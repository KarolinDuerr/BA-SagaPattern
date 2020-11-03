package saga.netflix.conductor.hotelservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.error.HotelServiceException;
import saga.netflix.conductor.hotelservice.model.HotelBooking;
import saga.netflix.conductor.hotelservice.model.HotelBookingInformation;
import saga.netflix.conductor.hotelservice.model.dto.BookHotelRequest;
import saga.netflix.conductor.hotelservice.model.dto.BookHotelResponse;
import saga.netflix.conductor.hotelservice.model.dto.HotelBookingDTO;

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

    @PostMapping
    public ResponseEntity<BookHotelResponse> bookHotel(@RequestBody final BookHotelRequest bookHotelRequest) throws HotelServiceException {
        logger.info("Book hotel: " + bookHotelRequest);

        if (bookHotelRequest == null || bookHotelRequest.getTravellerName() == null || bookHotelRequest.getTravellerName().isEmpty()) {
            logger.info("BookHotelRequest is missing or incomplete, therefore no hotel can be booked.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The information to book the hotel is missing or incomplete.");
        }

        HotelBookingInformation requestedHotelBooking = dtoConverter.convertToHotelBookingInformation(bookHotelRequest);
        HotelBooking receivedHotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), requestedHotelBooking);

        if (receivedHotelBooking == null) {
            logger.info("Something went wrong during booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during booking.");
        }

        return ResponseEntity.ok(new BookHotelResponse(receivedHotelBooking.getId(),
                receivedHotelBooking.getHotelName(), receivedHotelBooking.getBookingStatus().toString()));
    }
}