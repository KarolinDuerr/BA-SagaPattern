package saga.eventuate.tram.hotelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.hotelservice.error.BookingNotFound;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.BookingStatus;
import saga.eventuate.tram.hotelservice.model.HotelBooking;
import saga.eventuate.tram.hotelservice.model.HotelBookingInformation;
import saga.eventuate.tram.hotelservice.model.HotelBookingRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("HotelService")
@Transactional
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
    public HotelBooking getHotelBooking(Long bookingId) throws HotelException {
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
    public HotelBooking bookHotel(HotelBookingInformation hotelBooking) throws HotelException {
        logger.info("Saving the booked Hotel: " + hotelBooking);

        HotelBooking newHotelBooking = findAvailableHotel(hotelBooking);

        // no trip assigned therefore the booking has already been confirmed
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getTripId() == -1) {
            newHotelBooking.confirm();
        }

        hotelBookingRepository.save(newHotelBooking);

        return newHotelBooking;
    }

    @Override
    public boolean cancelHotelBooking(Long bookingId) throws HotelException {
        logger.info("Cancelling the booked hotel with ID " + bookingId);

        HotelBooking hotelBooking = getHotelBooking(bookingId);

        if (hotelBooking == null) {
            String message = String.format("The hotel booking (ID: %d) could not be updated.", bookingId);
            logger.info(message);
            throw new HotelException(ErrorType.INTERNAL_ERROR, message);
        }

        hotelBooking.cancel(BookingStatus.CANCELLED);
        hotelBookingRepository.save(hotelBooking);

        return true;
    }

    @Override
    public void cancelHotelBooking(Long bookingId, Long tripId) {
        logger.info("Cancelling the booked hotel with ID " + bookingId);

        HotelBooking hotelBooking;
        try {
            hotelBooking = getHotelBooking(bookingId);

            if (hotelBooking.getBookingInformation() == null || hotelBooking.getBookingInformation().getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            hotelBooking.cancel(BookingStatus.CANCELLED);
            hotelBookingRepository.save(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    @Override
    public void confirmHotelBooking(Long bookingId, Long tripId) {
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
    private HotelBooking findAvailableHotel(final HotelBookingInformation hotelBookingInformation) throws HotelException {
        if (hotelBookingInformation.getDestination().getCountry().equalsIgnoreCase("Provoke hotel failure")) {
            logger.info("Provoked hotel exception: no available hotel for trip: " + hotelBookingInformation.getTripId());
            throw new HotelException(ErrorType.NO_AVAILABLE_HOTEL, "No available flight found.");
        }

        return new HotelBooking("Example_Hotel", hotelBookingInformation);
    }
}
