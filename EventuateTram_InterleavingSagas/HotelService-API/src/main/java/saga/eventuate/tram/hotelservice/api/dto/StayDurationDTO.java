package saga.eventuate.tram.hotelservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StayDurationDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date arrival;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
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

    public Date getArrival() {
        return arrival;
    }

    public void setDeparture(final Date departure) {
        this.departure = departure;
    }

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
