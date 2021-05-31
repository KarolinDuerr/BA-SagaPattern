package saga.camunda.travelservice.model;

import saga.camunda.travelservice.error.ErrorType;
import saga.camunda.travelservice.error.TravelException;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class TripDuration {

    private Date start;

    private Date end;

    private TripDuration() {

    }

    public TripDuration(final Date start, final Date end) throws TravelException {
        validateDates(start, end);
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(final Date start) throws TravelException {
        validateDates(start, end);
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(final Date end) throws TravelException {
        validateDates(start, end);
        this.end = end;
    }

    private void validateDates(final Date start, final Date end) throws TravelException {
        if (start == null  || end == null) {
            return;
        }

        if (!start.before(end)) {
            throw new TravelException(ErrorType.INVALID_PARAMETER, "The departure is before the actual arrival.");
        }
    }

    @Override
    public String toString() {
        return "TripDuration{" +
                "startDate=" + start +
                ", endDate=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TripDuration)) {
            return false;
        }

        TripDuration tripDuration = (TripDuration) o;

        if (tripDuration.getStart().compareTo(this.getStart()) != 0 && tripDuration.getStart().getTime() != this.getStart().getTime()) {
            return false;
        }

        if (tripDuration.getEnd().compareTo(this.getEnd()) != 0 && tripDuration.getEnd().getTime() != this.getEnd().getTime()) {
            return false;
        }

        return true;
    }
}
