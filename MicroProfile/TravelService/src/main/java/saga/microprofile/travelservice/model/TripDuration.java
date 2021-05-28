package saga.microprofile.travelservice.model;

import saga.microprofile.travelservice.error.ErrorType;
import saga.microprofile.travelservice.error.TravelException;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Date;

@Embeddable
public class TripDuration {

    private LocalDate start;

    private LocalDate end;

    public TripDuration() {

    }

    public TripDuration(final LocalDate start, final LocalDate end) throws TravelException {
        validateDates(start, end);
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(final LocalDate start) throws TravelException {
        validateDates(start, end);
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(final LocalDate end) throws TravelException {
        validateDates(start, end);
        this.end = end;
    }

    private void validateDates(final LocalDate start, final LocalDate end) throws TravelException {
        if (start == null  || end == null) {
            return;
        }

        if (!start.isBefore(end)) {
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

        if (tripDuration.getStart().compareTo(this.getStart()) != 0) {
            return false;
        }

        if (tripDuration.getEnd().compareTo(this.getEnd()) != 0) {
            return false;
        }

        return true;
    }
}
