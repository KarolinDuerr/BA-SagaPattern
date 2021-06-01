package saga.eventuate.tram.hotelservice.controller;

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.hotelservice.error.BookingNotFound;
import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;
import saga.eventuate.tram.hotelservice.model.*;
import saga.eventuate.tram.hotelservice.saga.BookHotelEventSaga;
import saga.eventuate.tram.hotelservice.saga.BookHotelEventSagaData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("HotelService")
@Transactional
public class HotelService implements IHotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private final HotelBookingRepository hotelBookingRepository;


    @Autowired
    private final SagaInstanceFactory sagaInstanceFactory;

    @Autowired
    private final BookHotelEventSaga bookHotelEventSaga;

    public HotelService(final HotelBookingRepository hotelBookingRepository,
                        final SagaInstanceFactory sagaInstanceFactory, final BookHotelEventSaga bookHotelEventSaga) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.sagaInstanceFactory = sagaInstanceFactory;
        this.bookHotelEventSaga = bookHotelEventSaga;
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

        HotelBooking newHotelBooking = findAvailableHotel(travellerName, hotelBooking);

        // no trip and no event assigned therefore the booking has already been confirmed
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getTripId() == -1 && newHotelBooking.getBookingInformation().getEventId() == 0) {
            newHotelBooking.confirm();
        }

        // ensure idempotence of hotel bookings
        HotelBooking alreadyExistingHotelBooking = checkIfBookingAlreadyExists(newHotelBooking);
        if (alreadyExistingHotelBooking != null) {
            return alreadyExistingHotelBooking;
        }

        hotelBookingRepository.save(newHotelBooking);

        // book event if an event has been selected
        if (newHotelBooking.getBookingInformation() != null && newHotelBooking.getBookingInformation().getEventId() != 0) {
            // create the BookHotelEventSaga with the necessary information
            BookHotelEventSagaData sagaData = new BookHotelEventSagaData(newHotelBooking.getId(), travellerName,
                    hotelBooking.getEventId());
            sagaInstanceFactory.create(bookHotelEventSaga, sagaData);
        }

        return newHotelBooking;
    }

    @Override
    public void cancelHotelBooking(final Long bookingId, final Long tripId) {
        logger.info("Cancelling the booked hotel with ID " + bookingId);

        try {
            HotelBooking hotelBooking = getHotelBooking(bookingId);

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
    public void confirmHotelBooking(final Long bookingId, final Long tripId) {
        logger.info("Confirming the booked hotel with ID " + bookingId);

        try {
            HotelBooking hotelBooking = getHotelBooking(bookingId);

            if (hotelBooking.getBookingInformation() == null || hotelBooking.getBookingInformation().getTripId() != tripId) {
                throw new BookingNotFound(bookingId);
            }

            hotelBooking.confirm();
            hotelBookingRepository.save(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(bookingId);
        }
    }

    @Override
    public void confirmHotelEventBooking(final Long eventBookingId, final Long hotelBookingId) {
        logger.info("Confirming the booked event with ID " + eventBookingId);

        try {
            HotelBooking hotelBooking = getHotelBooking(hotelBookingId);

            hotelBooking.setEventBookingId(eventBookingId);
            hotelBookingRepository.save(hotelBooking);
        } catch (HotelException e) {
            throw new BookingNotFound(hotelBookingId);
        }
    }

    @Override
    public void rejectHotelEventBooking(final Long hotelBookingId, final RejectionReason rejectionReason) {
        logger.info("Rejecting the booked hotel with ID " + hotelBookingId + " due to failed event booking.");

        try {
            HotelBooking hotelBooking = getHotelBooking(hotelBookingId);

            BookingStatus newBookingStatus = convertToBookingStatus(rejectionReason);
            hotelBooking.reject(newBookingStatus);
            hotelBookingRepository.save(hotelBooking);
        } catch (HotelException exception) {
            throw new BookingNotFound(hotelBookingId);
        }
    }

    // only mocking the general function of this method
    private HotelBooking findAvailableHotel(final String travellerName,
                                            final HotelBookingInformation hotelBookingInformation) throws HotelException {
        if (hotelBookingInformation.getDestination().getCountry().equalsIgnoreCase("Provoke hotel failure")) {
            logger.info("Provoked hotel exception: no available hotel for trip: " + hotelBookingInformation.getTripId());
            throw new HotelException(ErrorType.NO_AVAILABLE_HOTEL, "No available hotel found.");
        }

        return new HotelBooking("Example_Hotel", travellerName, hotelBookingInformation);
    }

    // ensure idempotence of hotel bookings
    private HotelBooking checkIfBookingAlreadyExists(final HotelBooking hotelBooking) {
        List<HotelBooking> customerTrips =
                hotelBookingRepository.findByTravellerName(hotelBooking.getTravellerName());

        Optional<HotelBooking> savedHotelBooking =
                customerTrips.stream().filter(hotelInfo -> hotelInfo.equals(hotelBooking)).findFirst();

        if (!savedHotelBooking.isPresent()) {
            return null;
        }

        logger.info("Hotel has already been booked: " + savedHotelBooking.get());
        return savedHotelBooking.get();
    }

    private BookingStatus convertToBookingStatus(final RejectionReason rejectionReason) {
        if (Objects.equals(rejectionReason, RejectionReason.NO_EVENT_SPACE_AVAILABLE)) {
            return BookingStatus.REJECTED_NO_HOTEL_EVENT_AVAILABLE;
        }
        return BookingStatus.REJECTED_UNKNOWN;
    }
}
