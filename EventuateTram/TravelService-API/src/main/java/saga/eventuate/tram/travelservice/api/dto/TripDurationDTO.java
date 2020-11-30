package saga.eventuate.tram.travelservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class TripDurationDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotBlank(message = "Start date cannot be missing")
    @FutureOrPresent(message = "Date has to be in the present or the future.")
    private Date start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotBlank(message = "End date cannot be missing")
    @FutureOrPresent(message = "Date has to be in the present or the future.")
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
