package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import saga.camunda.travelservice.api.TravelServiceTopics;

@Component
public class BookTripAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(BookTripAdapter.class);

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        Long id = (Long) executionContext.getVariable(TravelServiceTopics.DataInput.BOOK_TRIP_ID);
        logger.info("BookTripSaga for trip" + id + " is being started.");
    }
}