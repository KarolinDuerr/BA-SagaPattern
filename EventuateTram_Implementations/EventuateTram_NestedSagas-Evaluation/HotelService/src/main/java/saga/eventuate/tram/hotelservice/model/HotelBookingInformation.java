package saga.eventuate.tram.hotelservice.model;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class HotelBookingInformation {

    @Embedded
    private Destination destination;

    @Embedded
    private StayDuration duration;

    private String boardType;

    private final long eventId;

    private final long tripId;

    private HotelBookingInformation() {
        tripId = -1; // no trip assigned to this booking
        eventId = 0; // no event booked for this booking
    }

    public HotelBookingInformation(final long tripId, final Destination destination, final StayDuration duration,
                                   final String boardType, final long eventId) {
        this.tripId = tripId;
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
        this.eventId = eventId;
    }

    public void setDestination(final Destination destination) {
        this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDuration(final StayDuration duration) {
        this.duration = duration;
    }

    public StayDuration getDuration() {
        return duration;
    }

    public void setBoardType(final String boardType) {
        this.boardType = boardType;
    }

    public String getBoardType() {
        return boardType;
    }

    public long getTripId() {
        return tripId;
    }

    public long getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "HotelBookingInformation{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
                ", eventId=" + eventId +
                ", tripId=" + tripId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (! (o instanceof HotelBookingInformation)) {
            return false;
        }

        HotelBookingInformation hotelInfo = (HotelBookingInformation) o;

        if (!Objects.equals(hotelInfo.getDuration(), this.getDuration())) {
            return false;
        }

        if (hotelInfo.getBoardType() == null || !hotelInfo.getBoardType().equalsIgnoreCase(this.getBoardType())) {
            return false;
        }

        if (!Objects.equals(hotelInfo.getDestination(), this.getDestination())) {
            return false;
        }

        if (!Objects.equals(hotelInfo.getEventId(), this.getEventId())) {
            return false;
        }

        return Objects.equals(hotelInfo.getTripId(), this.getTripId());
    }
}
