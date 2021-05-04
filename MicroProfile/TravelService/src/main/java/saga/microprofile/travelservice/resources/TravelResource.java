package saga.microprofile.travelservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import saga.microprofile.travelservice.api.dto.BookTripRequest;
import saga.microprofile.travelservice.api.dto.BookTripResponse;
import saga.microprofile.travelservice.controller.ITravelService;
import saga.microprofile.travelservice.error.TravelServiceException;
import saga.microprofile.travelservice.model.TripInformation;
import saga.microprofile.travelservice.model.dto.TripInformationDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/travel")
public class TravelResource {

    private static final Logger logger = LoggerFactory.getLogger(TravelResource.class);

    @Autowired
    private ITravelService travelService;

    @Autowired
    private DtoConverter dtoConverter;

    @GetMapping("/trips")
    public ResponseEntity<List<TripInformationDTO>> getTrips() throws TravelServiceException {
        logger.info("Get trips.");

        List<TripInformation> trips = travelService.getTripsInformation();

        if (trips == null) {
            logger.info("Something went wrong during receiving the trips information.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the trips information.");
        }

        return ResponseEntity.ok(dtoConverter.convertToTripInformationDTOList(trips));
    }

    @GetMapping("trips/{tripId}")
    public ResponseEntity<TripInformationDTO> getTrip(@PathVariable(value = "tripId") final Long tripId) throws TravelServiceException {
        logger.info("Get trip with ID: " + tripId);

        TripInformation tripInformation = travelService.getTripInformation(tripId);

        if (tripInformation == null) {
            logger.info("Something went wrong during receiving the trip information.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the trip information.");
        }

        return ResponseEntity.ok(dtoConverter.convertToTripInformationDTO(tripInformation));
    }

    @PostMapping
    public ResponseEntity<BookTripResponse> bookTrip(@RequestBody final BookTripRequest bookTripRequest) throws TravelServiceException {
        logger.info("Book trip: " + bookTripRequest);

        if (bookTripRequest == null) {
            logger.info("BookTripRequest is missing, therefore no trip can be booked.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The information to book the trip is missing.");
        }

        TripInformation tripInformation =
                travelService.bookTrip(dtoConverter.convertToTripInformation(bookTripRequest));

        if (tripInformation == null) {
            logger.info("Something went wrong during booking.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during booking.");
        }

        return ResponseEntity.ok(new BookTripResponse(tripInformation.getId(),
                tripInformation.getBookingStatus().toString()));
    }
}
