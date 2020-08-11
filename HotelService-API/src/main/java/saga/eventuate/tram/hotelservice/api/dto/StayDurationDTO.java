package saga.eventuate.tram.hotelservice.api.dto;

import java.util.Date;

public class StayDurationDTO {

    private Date arrival;

    private Date departure;

    public StayDurationDTO() {

    }

    public StayDurationDTO(final Date arrival, final Date departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    public void setArrvival(final Date arrival) {
        this.arrival = arrival;
    }

    public Date getArrvival() {
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
