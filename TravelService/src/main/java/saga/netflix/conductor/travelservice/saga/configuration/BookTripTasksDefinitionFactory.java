package saga.netflix.conductor.travelservice.saga.configuration;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;

import java.util.LinkedList;
import java.util.List;

/**
 * Creating and registering the task definitions for the BookTripSaga.
 */
@Component
public class BookTripTasksDefinitionFactory { // TODO: nur wenn eigene Worker und eigene WorkerTasks verwendet werden

    private static final Logger logger = LoggerFactory.getLogger(BookTripTasksDefinitionFactory.class);

    @Autowired
    private final MetadataClient metadataClient;

    private final List<TaskDef> taskDefinitions;

    public BookTripTasksDefinitionFactory(final MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
        taskDefinitions = new LinkedList<>();
        taskDefinitions.add(createBookHotelTask());
        taskDefinitions.add(createCancelHotelTask());
        taskDefinitions.add(createBookFlightTask());
        taskDefinitions.add(createCancelFlightTask());
        taskDefinitions.add(createConfirmHotelTask());
        taskDefinitions.add(createConfirmTripTask());
//        taskDefinitions.add(createRejectTripTask());
    }

    public void registerTaskDefinitions() {
        logger.info("Registering task definitions");

        // TODO FIX just for testing
        int counter = 0;
        boolean exceptionOccurred = true;
        while(exceptionOccurred && counter< 25) {
            try {
                this.metadataClient.registerTaskDefs(taskDefinitions);
                exceptionOccurred = false;
            } catch (RuntimeException exception) {
                logger.error("Exception occurred while registering task definitions: %s", exception.getMessage());
                try {
                    Thread.sleep(30000);
                } catch (Exception e) {
                    logger.info("exception");
                }
            }
        }
    }

    private TaskDef createBookHotelTask() {
        String description = String.format("'%s' task definition: invokes HotelService to book a hotel.",
                HotelServiceTasks.Task.BOOK_HOTEL);
        final TaskDef bookHotelDef = new TaskDef(HotelServiceTasks.Task.BOOK_HOTEL, description);
        bookHotelDef.setRetryCount(1);
        bookHotelDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        bookHotelDef.setRetryDelaySeconds(60);
        bookHotelDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        bookHotelDef.setResponseTimeoutSeconds(3600);
        bookHotelDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT);
        bookHotelDef.setInputKeys(inputKeys);

        final List<String> outputKeys = new LinkedList<>();
        outputKeys.add(HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT);
        bookHotelDef.setOutputKeys(outputKeys);

        return bookHotelDef;
    }

    private TaskDef createCancelHotelTask() {
        String description = String.format("'%s' task definition: invokes HotelService to cancel the hotel.",
                HotelServiceTasks.Task.CANCEL_HOTEL);
        final TaskDef cancelHotelDef = new TaskDef(HotelServiceTasks.Task.CANCEL_HOTEL, description);
        cancelHotelDef.setRetryCount(1);
        cancelHotelDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        cancelHotelDef.setRetryDelaySeconds(60);
        cancelHotelDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        cancelHotelDef.setResponseTimeoutSeconds(3600);
        cancelHotelDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT);
        cancelHotelDef.setInputKeys(inputKeys);

        return cancelHotelDef;
    }

    private TaskDef createBookFlightTask() {
        String description = String.format("'%s' task definition: invokes FlightService to book a flight.",
                FlightServiceTasks.Task.BOOK_FLIGHT);
        final TaskDef bookFlightDef = new TaskDef(FlightServiceTasks.Task.BOOK_FLIGHT, description);
        bookFlightDef.setRetryCount(1);
        bookFlightDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        bookFlightDef.setRetryDelaySeconds(60);
        bookFlightDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        bookFlightDef.setResponseTimeoutSeconds(3600);
        bookFlightDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT);
        bookFlightDef.setInputKeys(inputKeys);

        final List<String> outputKeys = new LinkedList<>();
        outputKeys.add(FlightServiceTasks.TaskOutput.BOOK_FLIGHT_OUTPUT);
        bookFlightDef.setOutputKeys(outputKeys);

        return bookFlightDef;
    }

    private TaskDef createCancelFlightTask() {
        String description = String.format("'%s' task definition: invokes FlightService to cancel the flight.",
                FlightServiceTasks.Task.CANCEL_FLIGHT);
        final TaskDef cancelHotelDef = new TaskDef(FlightServiceTasks.Task.CANCEL_FLIGHT, description);
        cancelHotelDef.setRetryCount(1);
        cancelHotelDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        cancelHotelDef.setRetryDelaySeconds(60);
        cancelHotelDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        cancelHotelDef.setResponseTimeoutSeconds(3600);
        cancelHotelDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT);
        cancelHotelDef.setInputKeys(inputKeys);

        return cancelHotelDef;
    }

    private TaskDef createConfirmHotelTask() {
        String description = String.format("'%s' task definition: invokes HotelService to confirm the hotel booking.",
                HotelServiceTasks.Task.CONFIRM_HOTEL);
        final TaskDef confirmHotelDef = new TaskDef(HotelServiceTasks.Task.CONFIRM_HOTEL, description);
        confirmHotelDef.setRetryCount(1);
        confirmHotelDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        confirmHotelDef.setRetryDelaySeconds(60);
        confirmHotelDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        confirmHotelDef.setResponseTimeoutSeconds(3600);
        confirmHotelDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(HotelServiceTasks.TaskInput.CONFIRM_HOTEL_INPUT);
        confirmHotelDef.setInputKeys(inputKeys);

        // TODO output notwendig?

        return confirmHotelDef;
    }

    private TaskDef createConfirmTripTask() {
        String description = String.format("'%s' task definition: invokes TripService to confirm the trip.",
                TravelServiceTasks.Task.CONFIRM_TRIP);
        final TaskDef confirmTripDef = new TaskDef(TravelServiceTasks.Task.CONFIRM_TRIP, description);
        confirmTripDef.setRetryCount(1);
        confirmTripDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
        confirmTripDef.setRetryDelaySeconds(60);
        confirmTripDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        confirmTripDef.setResponseTimeoutSeconds(3600);
        confirmTripDef.setOwnerEmail("travelService@beispielMail.com");

        final List<String> inputKeys = new LinkedList<>();
        inputKeys.add(TravelServiceTasks.TaskInput.CONFIRM_TRIP_HOTEL_INPUT);
        inputKeys.add(TravelServiceTasks.TaskInput.CONFIRM_TRIP_FLIGHT_INPUT);
        confirmTripDef.setInputKeys(inputKeys);

        return confirmTripDef;
    }

//    private TaskDef createRejectTripTask() {
//        String description = String.format("'%s' task definition: invokes TripService to reject the trip.",
//                TravelServiceTasks.Task.REJECT_TRIP);
//        final TaskDef confirmTripDef = new TaskDef(TravelServiceTasks.Task.REJECT_TRIP, description);
//        confirmTripDef.setRetryCount(1);
//        confirmTripDef.setRetryLogic(TaskDef.RetryLogic.FIXED);
//        confirmTripDef.setRetryDelaySeconds(60);
//        confirmTripDef.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
//        confirmTripDef.setResponseTimeoutSeconds(3600);
//        confirmTripDef.setOwnerEmail("travelService@beispielMail.com");
//
//        final List<String> inputKeys = new LinkedList<>();
//        inputKeys.add(TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT);
//        confirmTripDef.setInputKeys(inputKeys);
//
//        final List<String> outputKeys = new LinkedList<>();
//        outputKeys.add("lastTaskId"); //TODO: überhaupt was zurückgeben?
//        confirmTripDef.setOutputKeys(outputKeys);
//
//        return confirmTripDef;
//    }
}
