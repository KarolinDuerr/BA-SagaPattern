package saga.netflix.conductor.hotelservice.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class HotelBookingInformation {

    @Embedded
    private Destination destination;

    @Embedded
    private StayDuration duration;

    private String boardType;

    private final long tripId;

    private HotelBookingInformation() {
        tripId = -1; // no trip assigned to this booking
    }

    public HotelBookingInformation(final Destination destination, final StayDuration duration,
                                   final String boardType) {
        tripId = -1; // no trip assigned to this booking
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
    }

    public HotelBookingInformation(final long tripId, final Destination destination, final StayDuration duration,
                                   final String boardType) {
        this.tripId = tripId;
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
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

    @Override
    public String toString() {
        return "HotelBookingInformation{" +
                "destination=" + destination +
                ", duration=" + duration +
                ", boardType='" + boardType + '\'' +
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

        if (!hotelInfo.getDuration().equals(this.getDuration())) {
            return false;
        }

        if (!hotelInfo.getBoardType().equalsIgnoreCase(this.getBoardType())) {
            return false;
        }

        if (!hotelInfo.getDestination().equals(this.getDestination())) {
            return false;
        }

        return true;
    }
}
