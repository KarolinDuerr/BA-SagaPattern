package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.camunda.travelservice.api.TravelServiceTopics;
import saga.camunda.travelservice.controller.ITravelService;
import saga.camunda.travelservice.model.RejectionReason;

public class RejectTripAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(RejectTripAdapter.class);

    @Autowired
    private ITravelService travelService;

    public RejectTripAdapter(final ITravelService travelService) {
        this.travelService = travelService;
    }

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        Long tripId = (Long) executionContext.getVariable(TravelServiceTopics.DataInput.BOOK_TRIP_ID);
        String reason = (String) executionContext.getVariable(TravelServiceTopics.DataInput.REJECT_TRIP_REASON);
        RejectionReason rejectionReason = getRejectionReason(reason);
        travelService.rejectTrip(tripId, rejectionReason);

        logger.info("Rejected trip (ID: " + tripId + ") because: " + rejectionReason.toString());
    }

    private RejectionReason getRejectionReason(final String reason) {
        if (reason.contains(RejectionReason.NO_HOTEL_AVAILABLE.toString())) {
            return RejectionReason.NO_HOTEL_AVAILABLE;
        } else if (reason.contains(RejectionReason.NO_FLIGHT_AVAILABLE.toString())) {
            return RejectionReason.NO_FLIGHT_AVAILABLE;
        }
        return RejectionReason.REASON_UNKNOWN;
    }
}
