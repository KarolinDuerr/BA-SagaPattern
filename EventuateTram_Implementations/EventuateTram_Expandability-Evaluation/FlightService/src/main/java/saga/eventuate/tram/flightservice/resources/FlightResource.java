package saga.eventuate.tram.flightservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import saga.eventuate.tram.flightservice.controller.IFlightService;
import saga.eventuate.tram.flightservice.error.FlightServiceException;
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
    public ResponseEntity<List<FlightInformationDTO>> getFlightsInformation() throws FlightServiceException {
        logger.info("Get information about all flights.");

        List<FlightInformation> flightsInformation = flightService.getFlightsInformation();

        if (flightsInformation == null) {
            logger.info("Something went wrong during receiving the flights information.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the flights information.");
        }

        return ResponseEntity.ok(dtoConverter.convertToFlightInformationDTOList(flightsInformation));
    }

    @GetMapping("/bookings/{flightBookingId}")
    public ResponseEntity<FlightInformationDTO> getFlightInformation(@PathVariable(value = "flightBookingId") final Long flightBookingId) throws FlightServiceException {
        logger.info("Get flight booking with ID: " + flightBookingId);

        FlightInformation flightInformation = flightService.getFlightInformation(flightBookingId);

        if (flightInformation == null) {
            logger.info("Something went wrong during receiving the flight information.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during " +
                    "receiving the flight information.");
        }

        return ResponseEntity.ok(dtoConverter.convertToFlightInformationDTO(flightInformation));
    }
}
