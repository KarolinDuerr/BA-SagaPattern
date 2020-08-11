package saga.eventuate.tram.hotelservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
public class HotelService implements IHotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private final HotelBookingRepository hotelBookingRepository;

    public HotelService(HotelBookingRepository hotelBookingRepository) {
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
    public HotelBooking bookHotel(HotelBookingInformation hotelBooking) {
        logger.info("Saving the booked Hotel: " + hotelBooking);

        HotelBooking newHotelBooking = new HotelBooking("TestHotel", hotelBooking);
        hotelBookingRepository.save(newHotelBooking);

        return newHotelBooking;
    }

    @Override
    public HotelBooking bookHotel(HotelBookingInformation hotelBooking, int tripId) {
        logger.info(String.format("Saving the booked hotel: %s, with tripId= %d", hotelBooking, tripId));

        HotelBooking newHotelBooking = new HotelBooking("TestHotel", hotelBooking, tripId);
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

        hotelBooking.setBookingStatus(BookingStatus.CANCELLED);
        hotelBookingRepository.save(hotelBooking);

        return true;
    }
}
