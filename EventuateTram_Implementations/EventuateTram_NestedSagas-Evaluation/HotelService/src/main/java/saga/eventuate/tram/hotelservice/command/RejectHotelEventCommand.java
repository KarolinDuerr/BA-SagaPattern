package saga.eventuate.tram.hotelservice.command;

import io.eventuate.tram.commands.common.Command;
import saga.eventuate.tram.hotelservice.model.RejectionReason;

public class RejectHotelEventCommand implements Command {

    private final long hotelBookingId;

    private final RejectionReason rejectionReason;

    private RejectHotelEventCommand() {
        hotelBookingId = -1;
        rejectionReason = RejectionReason.REASON_UNKNOWN;
    }

    public RejectHotelEventCommand(final long hotelBookingId, final RejectionReason rejectionReason) {
        this.hotelBookingId = hotelBookingId;
        this.rejectionReason = rejectionReason;
    }

    public long getHotelBookingId() {
        return hotelBookingId;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public String toString() {
        return "RejectHotelEventCommand{" +
                "hotelBookingId=" + hotelBookingId +
                ", rejectionReason=" + rejectionReason +
                '}';
    }
}
