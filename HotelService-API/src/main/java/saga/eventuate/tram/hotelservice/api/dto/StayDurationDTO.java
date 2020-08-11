package saga.eventuate.tram.hotelservice.api.dto;

import java.util.Date;

public class StayDurationDTO {

    private Date startDate;

    private Date endDate;

    public StayDurationDTO() {

    }

    public StayDurationDTO(final Date startDate, final Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "DurationDTO{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
