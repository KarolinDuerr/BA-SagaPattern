package saga.microprofile.hotelservice.api.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class StayDurationDTO {

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate arrival;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate departure;

    // default constructor necessary for unmarshalling
    public StayDurationDTO() {

    }

    public StayDurationDTO(final LocalDate arrival, final LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    public void setArrival(final LocalDate arrival) {
        this.arrival = arrival;
    }

    @JsonbProperty("arrival")
    public LocalDate getArrival() {
        return arrival;
    }

    public void setDeparture(final LocalDate departure) {
        this.departure = departure;
    }

    @JsonbProperty("departure")
    public LocalDate getDeparture() {
        return departure;
    }

    @Override
    public String toString() {
        return "DurationDTO{" +
                "startDate=" + arrival +
                ", endDate=" + departure +
                '}';
    }
}
