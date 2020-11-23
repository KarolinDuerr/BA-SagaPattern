package saga.eventuate.tram.travelservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TripDurationDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public Date getStart() {
        return start;
    }

    public void setEnd(final Date end) {
        this.end = end;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
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
