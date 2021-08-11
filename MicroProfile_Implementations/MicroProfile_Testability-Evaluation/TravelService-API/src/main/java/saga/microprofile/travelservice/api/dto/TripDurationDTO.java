package saga.microprofile.travelservice.api.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

public class TripDurationDTO implements Serializable {

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotBlank(message = "Start date cannot be missing")
    @FutureOrPresent(message = "Date has to be in the present or the future.")
    private LocalDate start;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotBlank(message = "End date cannot be missing")
    @FutureOrPresent(message = "Date has to be in the present or the future.")
    private LocalDate end;

    public TripDurationDTO() {

    }

    public TripDurationDTO(final LocalDate start, final LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(final LocalDate start) {
        this.start = start;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setEnd(final LocalDate end) {
        this.end = end;
    }

    public LocalDate getEnd() {
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
