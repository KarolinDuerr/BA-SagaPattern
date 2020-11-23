package saga.netflix.conductor.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.customerservice.api.CustomerServiceTasks;
import saga.netflix.conductor.customerservice.error.CustomerServiceException;
import saga.netflix.conductor.customerservice.error.ErrorMessage;
import saga.netflix.conductor.customerservice.error.ErrorType;

import java.util.Map;

public class ValidateCustomerWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCustomerWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ICustomerService customerService;

    private final String inputValidateCustomer = CustomerServiceTasks.TaskInput.VALIDATE_CUSTOMER_INPUT;

    public ValidateCustomerWorker(final ObjectMapper objectMapper, final ICustomerService customerService) {
        this.objectMapper = objectMapper;
        this.customerService = customerService;
    }

    @Override
    public String getTaskDefName() {
        return CustomerServiceTasks.Task.VALIDATE_CUSTOMER;
    }

    @Override
    public TaskResult execute(Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(inputValidateCustomer)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    inputValidateCustomer);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            // prevent retry --> input would still be missing, so no reason to retry
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
            return taskResult;
        }

        logger.info("Task input: " + taskInput.get(inputValidateCustomer));
        final Long customerId = objectMapper.convertValue(taskInput.get(inputValidateCustomer), Long.class);

        try {
            customerService.validateCustomer(customerId);
            taskResult.setStatus(TaskResult.Status.COMPLETED);
        } catch (CustomerServiceException exception) {
            logger.error(exception.toString());
            taskResult.setReasonForIncompletion(exception.toString());
            taskResult.setStatus(TaskResult.Status.FAILED);
        }

        return taskResult;
    }
}
