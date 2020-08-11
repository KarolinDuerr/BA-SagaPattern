package saga.eventuate.tram.flightservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import saga.eventuate.tram.flightservice.api.dto.BookFlightRequest;
import saga.eventuate.tram.flightservice.api.dto.BookFlightResponse;
import saga.eventuate.tram.flightservice.controller.IFlightService;
import saga.eventuate.tram.flightservice.error.ConverterException;
import saga.eventuate.tram.flightservice.error.FlightException;
import saga.eventuate.tram.flightservice.model.FlightInformation;
import saga.eventuate.tram.flightservice.model.dto.FlightInformationDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/flights")
public class FlightResource {

    private static final Logger logger = LoggerFactory.getLogger(FlightResource.class);

    @Autowired
    private IFlightService flightService;

    @Autowired
    private DtoConverter dtoConverter;

    @GetMapping("/bookings")
    public ResponseEntity<List<FlightInformationDTO>> getFlightsInformation() throws ConverterException {
        logger.info("Get information about all flights.");

        List<FlightInformation> flightsInformation = flightService.getFlightsInformation();
        return ResponseEntity.ok(dtoConverter.convertToFlightInformationDTOList(flightsInformation));
    }

    @GetMapping("/bookings/{flightBookingId}")
    public ResponseEntity<FlightInformationDTO> getHotelBooking(@PathVariable(value = "flightBookingId") Long flightBookingId) throws ConverterException, FlightException {
        logger.info("Get flight booking with ID: " + flightBookingId);

        FlightInformation flightInformation = flightService.getFlightInformation(flightBookingId);
        return ResponseEntity.ok(dtoConverter.convertToFlightInformationDTO(flightInformation));
    }

    @PostMapping
    public ResponseEntity<BookFlightResponse> bookFlight(@RequestBody final BookFlightRequest bookFlightRequest) throws ConverterException, FlightException {
        logger.info("Book flight: " + bookFlightRequest);

        if (bookFlightRequest == null) {
            logger.info("BookFlightRequest is missing, therefore no hotel can be booked.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The information to book the flight is missing.");
        }

        FlightInformation flightInformation =
                flightService.bookFlight(dtoConverter.convertToFlightInformation(bookFlightRequest));

        return ResponseEntity.ok(new BookFlightResponse(flightInformation.getId(),
                flightInformation.getBookingStatus().toString()));
    }

    @DeleteMapping("/bookings/{flightBookingId}")
    public ResponseEntity cancelFlight(@PathVariable(value = "flightBookingId") Long flightBookingId) throws FlightException {
        logger.info("Cancel hotel booking with ID " + flightBookingId);

        boolean flightCancelled = flightService.cancelFlightBooking(flightBookingId);

        if (!flightCancelled) {
            logger.info("Something went wrong during cancellation.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "cancellation, flight could not be cancelled.");
        }

        return ResponseEntity.ok().build();
    }

}
