package saga.netflix.conductor.hotelservice.unittests.participant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelRequest;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.hotelservice.api.dto.DestinationDTO;
import saga.netflix.conductor.hotelservice.api.dto.StayDurationDTO;
import saga.netflix.conductor.hotelservice.controller.HotelService;
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.controller.worker.BookHotelWorker;
import saga.netflix.conductor.hotelservice.error.ErrorMessage;
import saga.netflix.conductor.hotelservice.error.ErrorType;
import saga.netflix.conductor.hotelservice.error.HotelException;
import saga.netflix.conductor.hotelservice.error.HotelServiceException;
import saga.netflix.conductor.hotelservice.model.BookingStatus;
import saga.netflix.conductor.hotelservice.model.HotelBooking;
import saga.netflix.conductor.hotelservice.model.HotelBookingInformation;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class BookHotelWorkerUnitTests {

    private BookHotelWorker bookHotelWorker;

    private Task mockedBookHotelTask;

    private IHotelService mockedHotelService;

    private BookHotelRequest bookHotelRequest;

    private final String inputBookHotel = HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT;
    private final Long hotelId = 1L;
    private final String hotelName = "Test Hotel";

    @Before
    public void setUp() throws ParseException, HotelServiceException {
        ObjectMapper mockedObjectMapper = Mockito.mock(ObjectMapper.class);
        DtoConverter dtoConverter = new DtoConverter();
        HotelBooking mockedHotelBooking = Mockito.mock(HotelBooking.class);
        this.mockedHotelService = Mockito.mock(HotelService.class);
        this.bookHotelWorker = new BookHotelWorker(mockedObjectMapper, mockedHotelService, dtoConverter);
        this.mockedBookHotelTask = Mockito.mock(Task.class);
        this.bookHotelRequest = createBookHotelRequest();

        // Stubs
        Mockito.when(mockedObjectMapper.convertValue(Mockito.any(BookHotelRequest.class),
                Mockito.eq(BookHotelRequest.class))).thenReturn(bookHotelRequest);
        Mockito.when(mockedHotelService.bookHotel(Mockito.anyString(), Mockito.any(HotelBookingInformation.class))).thenReturn(mockedHotelBooking);
        Mockito.when(mockedHotelBooking.getId()).thenReturn(hotelId);
        Mockito.when(mockedHotelBooking.getHotelName()).thenReturn(hotelName);
        Mockito.when(mockedHotelBooking.getBookingStatus()).thenReturn(BookingStatus.PENDING);
        stubHotelTask();
    }

    @Test
    public void bookHotelTaskShouldBookHotel() throws HotelException {
        // setup
        TaskResult taskResult = new TaskResult(mockedBookHotelTask);
        BookHotelResponse bookingResponse = new BookHotelResponse(bookHotelRequest.getTripId(), hotelId, hotelName,
                BookingStatus.PENDING.toString());
        taskResult.getOutputData().put(HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT, bookingResponse);
        taskResult.setStatus(TaskResult.Status.COMPLETED);

        // execute
        TaskResult receivedTaskResult = bookHotelWorker.execute(mockedBookHotelTask);

        // verify
        Mockito.verify(mockedHotelService, Mockito.times(1)).bookHotel(Mockito.anyString(),
                Mockito.any(HotelBookingInformation.class));
        // TaskResult does not provide equals method --> directly comparing of the two TaskResults not possible
        // Important ones:
        Assert.assertEquals(taskResult.getOutputData(), receivedTaskResult.getOutputData());
        Assert.assertEquals(taskResult.getReasonForIncompletion(), receivedTaskResult.getReasonForIncompletion());
        Assert.assertEquals(taskResult.getStatus(), receivedTaskResult.getStatus());
        Assert.assertEquals(taskResult.getCallbackAfterSeconds(), receivedTaskResult.getCallbackAfterSeconds());
        Assert.assertEquals(taskResult.getOutputMessage(), receivedTaskResult.getOutputMessage());
        Assert.assertEquals(taskResult.getLogs(), receivedTaskResult.getLogs());
        // Additional checks to be sure (values should not have changed):
        Assert.assertEquals(taskResult.getWorkflowInstanceId(), receivedTaskResult.getWorkflowInstanceId());
        Assert.assertEquals(taskResult.getTaskId(), receivedTaskResult.getTaskId());
        Assert.assertEquals(taskResult.getWorkerId(), receivedTaskResult.getWorkerId());
        Assert.assertEquals(taskResult.getExternalOutputPayloadStoragePath(),
                receivedTaskResult.getExternalOutputPayloadStoragePath());
        Assert.assertEquals(taskResult.getSubWorkflowId(), receivedTaskResult.getSubWorkflowId());
    }


    @Test
    public void bookHotelTaskMissingInputDataShouldReturnFailedTaskStatus() throws HotelException {
        // setup
        Task mockedFailingBookHotelTask = Mockito.mock(Task.class);
        Mockito.when(mockedFailingBookHotelTask.getStatus()).thenReturn(Task.Status.SCHEDULED);
        TaskResult taskResult = new TaskResult(mockedFailingBookHotelTask);
        taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
        String errorMessage = String.format("%s: misses the necessary input data (%s)",
                HotelServiceTasks.Task.BOOK_HOTEL, HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT);
        taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());

        // execute
        TaskResult receivedTaskResult = bookHotelWorker.execute(mockedFailingBookHotelTask);

        // verify
        Mockito.verify(mockedHotelService, Mockito.times(0)).bookHotel(Mockito.anyString(),
                Mockito.any(HotelBookingInformation.class));
        // TaskResult does not provide equals method --> directly comparing of the two TaskResults not possible
        // Important ones:
        Assert.assertEquals(taskResult.getOutputData(), receivedTaskResult.getOutputData());
        Assert.assertEquals(taskResult.getReasonForIncompletion(), receivedTaskResult.getReasonForIncompletion());
        Assert.assertEquals(taskResult.getStatus(), receivedTaskResult.getStatus());
        Assert.assertEquals(taskResult.getCallbackAfterSeconds(), receivedTaskResult.getCallbackAfterSeconds());
        Assert.assertEquals(taskResult.getOutputMessage(), receivedTaskResult.getOutputMessage());
        Assert.assertEquals(taskResult.getLogs(), receivedTaskResult.getLogs());
        // Additional checks to be sure (values should not have changed):
        Assert.assertEquals(taskResult.getWorkflowInstanceId(), receivedTaskResult.getWorkflowInstanceId());
        Assert.assertEquals(taskResult.getTaskId(), receivedTaskResult.getTaskId());
        Assert.assertEquals(taskResult.getWorkerId(), receivedTaskResult.getWorkerId());
        Assert.assertEquals(taskResult.getExternalOutputPayloadStoragePath(),
                receivedTaskResult.getExternalOutputPayloadStoragePath());
        Assert.assertEquals(taskResult.getSubWorkflowId(), receivedTaskResult.getSubWorkflowId());
    }

    @Test
    public void bookHotelTaskFailureInputShouldReturnFailedTaskStatus() throws HotelException {
        // setup
        TaskResult taskResult = new TaskResult(mockedBookHotelTask);
        HotelException hotelException = new HotelException(ErrorType.NO_HOTEL_AVAILABLE, "Testing Hotel not available case");
        Mockito.when(mockedHotelService.bookHotel(Mockito.anyString(), Mockito.any(HotelBookingInformation.class))).thenThrow(hotelException);
        taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
        taskResult.setReasonForIncompletion(hotelException.toString());

        // execute
        TaskResult receivedTaskResult = bookHotelWorker.execute(mockedBookHotelTask);

        // verify
        Mockito.verify(mockedHotelService, Mockito.times(1)).bookHotel(Mockito.anyString(),
                Mockito.any(HotelBookingInformation.class));
        // TaskResult does not provide equals method --> directly comparing of the two TaskResults not possible
        Assert.assertEquals(taskResult.getWorkflowInstanceId(), receivedTaskResult.getWorkflowInstanceId());
        Assert.assertEquals(taskResult.getTaskId(), receivedTaskResult.getTaskId());
        Assert.assertEquals(taskResult.getReasonForIncompletion(), receivedTaskResult.getReasonForIncompletion());
        Assert.assertEquals(taskResult.getCallbackAfterSeconds(), receivedTaskResult.getCallbackAfterSeconds());
        Assert.assertEquals(taskResult.getWorkerId(), receivedTaskResult.getWorkerId());
        Assert.assertEquals(taskResult.getOutputData(), receivedTaskResult.getOutputData());
        Assert.assertEquals(taskResult.getExternalOutputPayloadStoragePath(),
                receivedTaskResult.getExternalOutputPayloadStoragePath());
        Assert.assertEquals(taskResult.getSubWorkflowId(), receivedTaskResult.getSubWorkflowId());
        Assert.assertEquals(taskResult.getStatus(), receivedTaskResult.getStatus());
        Assert.assertEquals(taskResult.getOutputMessage(), receivedTaskResult.getOutputMessage());
        Assert.assertEquals(taskResult.getLogs(), receivedTaskResult.getLogs());
    }

    private void stubHotelTask() {
        Mockito.when(mockedBookHotelTask.getWorkflowInstanceId()).thenReturn("123456");
        Mockito.when(mockedBookHotelTask.getTaskId()).thenReturn("1234");
        Mockito.when(mockedBookHotelTask.getReasonForIncompletion()).thenReturn("");
        Mockito.when(mockedBookHotelTask.getCallbackAfterSeconds()).thenReturn(0L);
        Mockito.when(mockedBookHotelTask.getWorkerId()).thenReturn("TestWorker1");
        Mockito.when(mockedBookHotelTask.getOutputData()).thenReturn(new HashMap<>());
        Mockito.when(mockedBookHotelTask.getExternalOutputPayloadStoragePath()).thenReturn("");
        Mockito.when(mockedBookHotelTask.getSubWorkflowId()).thenReturn("");
        Mockito.when(mockedBookHotelTask.getStatus()).thenReturn(Task.Status.SCHEDULED);

        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put(inputBookHotel, bookHotelRequest);
        Mockito.when(mockedBookHotelTask.getInputData()).thenReturn(taskInput);

    }

    private BookHotelRequest createBookHotelRequest() throws ParseException {
        DestinationDTO destination = new DestinationDTO("Test Country", "Test City");
        StayDurationDTO duration = new StayDurationDTO(Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2021"
        ).toInstant()),
                Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("12-12-2021").toInstant()));
        return new BookHotelRequest(1L, destination, duration, "breakfast", "Test traveller");
    }
}
