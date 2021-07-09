package saga.eventuate.tram.hotelservice.model;

import saga.eventuate.tram.hotelservice.error.ErrorType;
import saga.eventuate.tram.hotelservice.error.HotelException;

import javax.persistence.Embeddable;
import java.time.Duration;
import java.util.Date;

@Embeddable
public class StayDuration {

    private Date arrival;

    private Date departure;

    private long numberOfNights;

    private StayDuration() {

    }

    public StayDuration(final Date arrival, final Date departure) throws HotelException {
        validateDates(arrival, departure);

        this.arrival = arrival;
        this.departure = departure;
        this.numberOfNights = calculateNumberOfNights();
    }

    public void setArrival(final Date arrival) throws HotelException {
        validateDates(arrival, departure);
        this.arrival = arrival;
        this.numberOfNights = calculateNumberOfNights();
    }

    public Date getArrival() {
        return arrival;
    }

    public void setDeparture(final Date departure) throws HotelException {
        validateDates(arrival, departure);
        this.departure = departure;
        this.numberOfNights = calculateNumberOfNights();
    }

    public Date getDeparture() {
        return departure;
    }

    public long getNumberOfNights() {
        return numberOfNights;
    }

    private void validateDates(final Date arrival, final Date departure) throws HotelException {
        if (arrival == null  || departure == null) {
            return;
        }

        if (!arrival.before(departure)) {
            throw new HotelException(ErrorType.INVALID_PARAMETER, "The departure is before the actual arrival.");
        }
    }

    private long calculateNumberOfNights() {
        Duration duration = Duration.between(arrival.toInstant(), departure.toInstant());
        return duration == null ? 0 : duration.toDays() - 1;
    }

    @Override
    public String toString() {
        return "StayDuration{" +
                "startDate=" + arrival +
                ", endDate=" + departure +
                ", numberOfNights=" + numberOfNights +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (! (o instanceof StayDuration)) {
            return false;
        }

        StayDuration stayDuration = (StayDuration) o;

        if (stayDuration.getDeparture().compareTo(this.getDeparture()) != 0 && stayDuration.getDeparture().getTime() != this.getDeparture().getTime()) {
            return false;
        }

        if (stayDuration.getArrival().compareTo(this.getArrival()) != 0 && stayDuration.getArrival().getTime() != this.getArrival().getTime()) {
            return false;
        }

        return stayDuration.getNumberOfNights() == this.getNumberOfNights();
    }
}
