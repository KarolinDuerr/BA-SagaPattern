package saga.microprofile.hotelservice.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class HotelBookingInformation {

    @Embedded
    private Destination destination;

    @Embedded
    private StayDuration duration;

    private String boardType;

    private final long tripId;

    private String lraId;

    private HotelBookingInformation() {
        tripId = -1; // no trip assigned to this booking
        lraId = ""; // no LRA assigned to this booking
    }

    public HotelBookingInformation(final Destination destination, final StayDuration duration,
                                   final String boardType) {
        this.tripId = -1; // no trip assigned to this booking
        this.lraId = "";
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
    }

    public HotelBookingInformation(final long tripId, final URI lraId, final Destination destination, final StayDuration duration,
                                   final String boardType) {
        this.tripId = tripId;
        this.lraId = lraId.toString();
        this.destination = destination;
        this.duration = duration;
        this.boardType = boardType;
    }

    public String getLraId() {
        return lraId;
    }

    public void setLraId(String lraId) {
        this.lraId = lraId;
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
                ", lraId='" + lraId + '\'' +
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

        return Objects.equals(hotelInfo.getTripId(), this.getTripId());
    }
}

