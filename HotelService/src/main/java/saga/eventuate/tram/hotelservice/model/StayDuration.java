package saga.eventuate.tram.hotelservice.model;

import javax.persistence.Embeddable;
import java.time.Duration;
import java.util.Date;

@Embeddable
public class StayDuration {

    private Date startDate;

    private Date endDate;

    private long numberOfNights;

    public StayDuration() {

    }

    public StayDuration(final Date startDate, final Date endDate) {
        validateDates(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfNights = calculateNumberOfNights();
    }

    public void setStartDate(final Date startDate) {
        validateDates(startDate, endDate);
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(final Date endDate) {
        validateDates(startDate, endDate);
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setNumberOfNights(final long numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public long getNumberOfNights() {
        return numberOfNights;
    }

    private void validateDates(Date startDate, Date endDate) {
        if (!startDate.before(endDate)) {
            // TODO throw Exception
        }
    }

    private long calculateNumberOfNights() {
        Duration duration = Duration.between(startDate.toInstant(), endDate.toInstant());
        return duration == null ? 0 : duration.toDays() - 1;
    }

    @Override
    public String toString() {
        return "StayDuration{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", numberOfNights=" + numberOfNights +
                '}';
    }
}
