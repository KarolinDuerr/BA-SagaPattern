package saga.netflix.conductor.travelservice.saga.bookTripSaga;

import com.netflix.conductor.client.exceptions.ConductorClientException;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.saga.Sagas;
import saga.netflix.conductor.travelservice.saga.configuration.BookTripTasksFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Create and register the workflow definition for the BookTripSaga and its compensating workflow.
 */
public class BookTripSaga {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    @Autowired
    private final MetadataClient metadataClient;

    private final BookTripTasksFactory taskInstanceFactory;

    public BookTripSaga(final MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
        this.taskInstanceFactory = new BookTripTasksFactory(this.metadataClient);

        final List<WorkflowDef> workflowDefinitions = new LinkedList<>();
        workflowDefinitions.add(createCompensateBookTripSagaWorkflowDef());
        workflowDefinitions.add(createSagaBookTripWorkflowDef());
        logger.info("Registering Workflow definition");

        // TODO FIX just for testing
        try {
            metadataClient.updateWorkflowDefs(workflowDefinitions);
        } catch(ConductorClientException conductorClientException) {
            logger.error("Exception occurred while registering workflow definitions: %s", conductorClientException.getMessage());
            logger.error(conductorClientException.getStackTrace().toString());
        }
    }

    private WorkflowDef createCompensateBookTripSagaWorkflowDef() {
        final WorkflowDef compensateBookTripSaga = new WorkflowDef();
        compensateBookTripSaga.setName(Sagas.COMP_BOOK_TRIP_SAGA);
        compensateBookTripSaga.setDescription("Workflow for compensating the Saga of booking a trip");
        compensateBookTripSaga.setVersion(1);

        List<WorkflowTask> compensateSagaBookTripTasks = new LinkedList<>();
        compensateSagaBookTripTasks.add(taskInstanceFactory.cancelHotelTask());
        compensateSagaBookTripTasks.add(taskInstanceFactory.rejectTripTask());
        //TODO
        compensateBookTripSaga.setTasks(compensateSagaBookTripTasks);

//        List<String> inputParameters = new LinkedList<>(); // TODO: notwendig? --> Output notwendig?
//        bookTripSaga.setInputParameters();

        compensateBookTripSaga.setSchemaVersion(2);
        compensateBookTripSaga.setRestartable(true); // has to be --> was bei failure?
        compensateBookTripSaga.setOwnerEmail("travelService@beispielMail.com");
        return compensateBookTripSaga;
    }

    private WorkflowDef createSagaBookTripWorkflowDef() {
        final WorkflowDef bookTripSaga = new WorkflowDef();
        bookTripSaga.setName(Sagas.BOOK_TRIP_SAGA);
        bookTripSaga.setDescription("Workflow for Saga of booking a trip");
        bookTripSaga.setVersion(1);

        List<WorkflowTask> sagaBookTripTasks = new LinkedList<>();
        sagaBookTripTasks.add(taskInstanceFactory.bookHotelTask());
        // TODO
        bookTripSaga.setTasks(sagaBookTripTasks);

        List<String> inputParameters = new LinkedList<>(); // TODO: Output notwendig?
        inputParameters.add(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT);
        // TODO
        bookTripSaga.setInputParameters(inputParameters);

        bookTripSaga.setFailureWorkflow(Sagas.COMP_BOOK_TRIP_SAGA);
        bookTripSaga.setSchemaVersion(2);
        bookTripSaga.setRestartable(true);
        bookTripSaga.setOwnerEmail("travelService@beispielMail.com");
        return bookTripSaga;
    }

}
