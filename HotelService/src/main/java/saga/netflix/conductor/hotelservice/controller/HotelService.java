package saga.netflix.conductor.hotelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.netflix.conductor.hotelservice.error.BookingNotFound;
import saga.netflix.conductor.hotelservice.error.ErrorType;
import saga.netflix.conductor.hotelservice.error.HotelException;
import saga.netflix.conductor.hotelservice.model.BookingStatus;
import saga.netflix.conductor.hotelservice.model.HotelBooking;
import saga.netflix.conductor.hotelservice.model.HotelBookingInformation;
import saga.netflix.conductor.hotelservice.model.HotelBookingRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("HotelService")
public class HotelService implements IHotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private final HotelBookingRepository hotelBookingRepository;

    public HotelService(final HotelBookingRepository hotelBookingRepository) {
        this.hotelBookingRepository = hotelBookingRepository;
    }

    @Override
    public List<HotelBooking> getHotelBookings() {
        logger.info("Get hotel bookings from Repository.");

        List<HotelBooking> hotelBookings = new LinkedList<>();

        Iterable<HotelBooking> bookings = hotelBookingRepository.findAll();

        for (HotelBooking booking : bookings) {
            hotelBookings.add(booking);
        }

        return hotelBookings;
    }

    @Override
    public HotelBooking getHotelBooking(final Long bookingId) throws HotelException {
        logger.info(String.format("Get hotel booking (ID: %d) from Repository.", bookingId));

        Optional<HotelBooking> hotelBooking = hotelBookingRepository.findById(bookingId);

        if (!hotelBooking.isPresent()) {
            String message = String.format("The hotel booking (ID: %d) does not exist.", bookingId);
            logger.info(message);
            throw new HotelException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return hotelBooking.get();
    }

    @Override
    public HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException {
        logger.info("Saving the booked Hotel: " + hotelBooking);

        HotelBooking newHotelBooking = findAvailableHotel(travellerName, hotelBooking); // TODO --> unterscheiden
        // zwischen normaler HotelBuchung und TripBuchung? siehe flights

        // no trip assigned therefore the booking has already been confirmed
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getTripId() == -1) {
            newHotelBooking.confirm();
        }

        //ensure idempotence of hotel bookings
        HotelBooking alreadyExistingHotelBooking = checkIfBookingAlreadyExists(newHotelBooking);
        if (alreadyExistingHotelBooking != null) {
            return alreadyExistingHotelBooking;
        }

        hotelBookingRepository.save(newHotelBooking);

        return newHotelBooking;
    }

    @Override
    public void cancelHotelBooking(final long tripId, final String travellerName) {
        logger.info("Cancelling the booked hotel associated with trip ID " + tripId);

        HotelBooking hotelBooking;
        hotelBooking = geTripHotelBookingByName(travellerName, tripId);

        if (hotelBooking == null) {
            logger.info(String.format("No hotel has been booked for this trip (ID: %s) yet, therefore no need to cancel.", tripId));
            // no hotel has been booked for this trip yet, therefore no need to cancel
            return;
        }

        hotelBooking.cancel(BookingStatus.CANCELLED);
        hotelBookingRepository.save(hotelBooking);
    }

    @Override
    public void confirmHotelBooking(final Long bookingId, final Long tripId) {
        logger.info("Confirming the booked hotel with ID " + bookingId);

        HotelBooking hotelBooking;
        try {
            hotelBooking = getHotelBooking(bookingId);

            if (hotelBooking.getBookingInformation() == null || hotelBooking.getBookingInformation().getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            hotelBooking.confirm();
            hotelBookingRepository.save(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    // only mocking the general function of this method
    private HotelBooking findAvailableHotel(final String travellerName,
                                            final HotelBookingInformation hotelBookingInformation) throws HotelException {
        if (hotelBookingInformation.getDestination().getCountry().equalsIgnoreCase("Provoke hotel failure")) {
            logger.info("Provoked hotel exception: no available hotel for trip: " + hotelBookingInformation.getTripId());
            throw new HotelException(ErrorType.NO_AVAILABLE_HOTEL, "No available flight found.");
        }

        return new HotelBooking("Example_Hotel", travellerName, hotelBookingInformation);
    }

    //ensure idempotence of hotel bookings
    private HotelBooking checkIfBookingAlreadyExists(final HotelBooking hotelBooking) {
        List<HotelBooking> customerTrips =
                hotelBookingRepository.findByTravellerName(hotelBooking.getTravellerName());

        Optional<HotelBooking> savedHotelBooking =
                customerTrips.stream().filter(hotelInfo -> hotelInfo.equals(hotelBooking)).findFirst();

        if (!savedHotelBooking.isPresent()) {
            return null;
        }

        logger.info("Hotel has already been booked: " + savedHotelBooking.toString());
        return savedHotelBooking.get();
    }

    private HotelBooking geTripHotelBookingByName(final String travellerName, final long tripId) {
        List<HotelBooking> customerTrips =
                hotelBookingRepository.findByTravellerName(travellerName);

        Optional<HotelBooking> existingHotelBooking =
                customerTrips.stream().filter(hotelInfo -> hotelInfo.getBookingInformation().getTripId() == tripId).findFirst();

        if (!existingHotelBooking.isPresent()) {
            // no hotel has been booked for this trip yet, therefore no need to cancel
            return null;
        }

        return existingHotelBooking.get();
    }
}
