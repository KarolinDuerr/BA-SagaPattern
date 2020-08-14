package saga.eventuate.tram.travelservice.api.dto;

import java.util.Date;

public class TripDurationDTO {

    private Date start;

    private Date end;

    private TripDurationDTO() {

    }

    public TripDurationDTO(final Date start, final Date end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(final Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public void setEnd(final Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "TripDurationDTO{" +
                "startDate=" + start +
                ", endDate=" + end +
                '}';
    }
}
