package saga.microprofile.hotelservice.api.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Date;

public class StayDurationDTO {

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private Date arrival;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private Date departure;

    private StayDurationDTO() {

    }

    public StayDurationDTO(final Date arrival, final Date departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    public void setArrival(final Date arrival) {
        this.arrival = arrival;
    }

    @JsonbProperty("arrival")
    public Date getArrival() {
        return arrival;
    }

    public void setDeparture(final Date departure) {
        this.departure = departure;
    }

    @JsonbProperty("departure")
    public Date getDeparture() {
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
