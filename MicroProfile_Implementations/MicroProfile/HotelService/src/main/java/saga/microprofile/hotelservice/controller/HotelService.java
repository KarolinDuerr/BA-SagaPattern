package saga.microprofile.hotelservice.controller;

import saga.microprofile.hotelservice.error.BookingNotFound;
import saga.microprofile.hotelservice.error.ErrorType;
import saga.microprofile.hotelservice.error.HotelException;
import saga.microprofile.hotelservice.model.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
@HotelServiceImpl
public class HotelService implements IHotelService {

    private static final Logger logger = Logger.getLogger(HotelService.class.toString());

    @Inject
    private HotelBookingRepository hotelBookingRepository;

    public HotelService() {
    }

//    public HotelService(final HotelBookingRepository hotelBookingRepository) {
//        this.hotelBookingRepository = hotelBookingRepository;
//    }

    @Override
//    @Transactional
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
//    @Transactional
    public HotelBooking getHotelBooking(final Long bookingId) throws HotelException {
        logger.info(String.format("Get hotel booking (ID: %d) from Repository.", bookingId));

        HotelBooking hotelBooking = hotelBookingRepository.findById(bookingId);

        if (hotelBooking == null) {
            String message = String.format("The hotel booking (ID: %d) does not exist.", bookingId);
            logger.info(message);
            throw new HotelException(ErrorType.NON_EXISTING_ITEM, message);
        }

        return hotelBooking;
    }

    @Override
//    @Transactional
    public HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException {
        logger.info("Saving the booked Hotel: " + hotelBooking);

        HotelBooking newHotelBooking = findAvailableHotel(travellerName, hotelBooking);

        // no trip assigned therefore the booking has already been confirmed
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getTripId() == -1) {
            newHotelBooking.confirm();
        }

        // ensure idempotence of hotel bookings
        HotelBooking alreadyExistingHotelBooking = checkIfBookingAlreadyExists(newHotelBooking);
        if (alreadyExistingHotelBooking != null) {
            return alreadyExistingHotelBooking;
        }

        hotelBookingRepository.save(newHotelBooking);

        return newHotelBooking;
    }

    @Override
//    @Transactional
    public void cancelHotelBooking(final URI lraId) {
        logger.info("Cancelling the booked hotel associated with LRA ID " + lraId);

        try {
            HotelBooking hotelBooking = findBookingByLraId(lraId);

            if (hotelBooking.getBookingInformation() == null) {
                throw new BookingNotFound(lraId);
            }

            hotelBooking.cancel(BookingStatus.CANCELLED);
            hotelBookingRepository.update(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(lraId);
        }
    }

    @Override
//    @Transactional
    public void confirmHotelBooking(final Long bookingId, final Long tripId) {
        logger.info("Confirming the booked hotel with ID " + bookingId);

        try {
            HotelBooking hotelBooking = getHotelBooking(bookingId);

            if (hotelBooking.getBookingInformation() == null || hotelBooking.getBookingInformation().getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            hotelBooking.confirm();
            hotelBookingRepository.update(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    // only mocking the general function of this method
    private HotelBooking findAvailableHotel(final String travellerName,
                                            final HotelBookingInformation hotelBookingInformation) throws HotelException {
        if (hotelBookingInformation.getDestination().getCountry().equalsIgnoreCase("Provoke hotel failure")) {
            logger.info("Provoked hotel exception: no available hotel for trip: " + hotelBookingInformation.getTripId());
            throw new HotelException(ErrorType.NO_HOTEL_AVAILABLE, "No available hotel found.");
        }

        return new HotelBooking("Example_Hotel", travellerName, hotelBookingInformation);
    }

    // ensure idempotence of hotel bookings
    private HotelBooking checkIfBookingAlreadyExists(final HotelBooking hotelBooking) {
        List<HotelBooking> customerHotelBookings =
                hotelBookingRepository.findByTravellerName(hotelBooking.getTravellerName());

        Optional<HotelBooking> savedHotelBooking =
                customerHotelBookings.stream().filter(hotelInfo -> hotelInfo.equals(hotelBooking)).findFirst();

        if (!savedHotelBooking.isPresent()) {
            return null;
        }

        logger.info("Hotel has already been booked: " + savedHotelBooking.toString());
        return savedHotelBooking.get();
    }

    // enable compensation of bookings related with a LRA
    private HotelBooking findBookingByLraId(final URI lraId) throws HotelException {
        List<HotelBooking> customerHotelBookings =
                hotelBookingRepository.findByLraId(lraId.toString());

        Optional<HotelBooking> savedHotelBooking = customerHotelBookings.stream().findFirst(); // TODO check

        if (!savedHotelBooking.isPresent()) {
            throw new HotelException(ErrorType.NON_EXISTING_ITEM, "Related trip could not be found");
        }

        logger.info("Related hotel booking has been found: " + savedHotelBooking);
        return savedHotelBooking.get();
    }
}
