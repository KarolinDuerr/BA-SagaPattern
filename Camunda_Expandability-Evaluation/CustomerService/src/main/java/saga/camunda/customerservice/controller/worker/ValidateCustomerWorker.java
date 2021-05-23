package saga.camunda.customerservice.controller.worker;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.camunda.customerservice.api.CustomerServiceTopics;
import saga.camunda.customerservice.controller.ICustomerService;
import saga.camunda.customerservice.error.CustomerServiceException;

@Component
@ExternalTaskSubscription("validateCustomer")
public class ValidateCustomerWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCustomerWorker.class);

    @Autowired
    private final ICustomerService customerService;

    public ValidateCustomerWorker(final ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void execute(final ExternalTask externalTask, final ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        logger.info("Validating customer with ID: " + externalTask.getVariable(CustomerServiceTopics.DataInput.VALIDATE_CUSTOMER_DATA));
        TypedValue customerIdValue = externalTask.getVariableTyped(CustomerServiceTopics.DataInput.VALIDATE_CUSTOMER_DATA); // TODO
        logger.info("Validating customer with ID2: " + customerIdValue);

        if (customerIdValue == null || customerIdValue.getValue() == null) {
            logger.info("The given input could not be parsed to receive the customer ID.");
            externalTaskService.handleBpmnError(externalTask, CustomerServiceTopics.BpmnError.CUSTOMER_ERROR, "Something went" +
                    " wrong with the given input.");
            externalTaskService.complete(externalTask, null);
            return;
        }

        try {
            customerService.validateCustomer(Long.getLong("2"));
            externalTaskService.complete(externalTask, null);
        } catch (CustomerServiceException exception) {
            logger.error(exception.toString());
            externalTaskService.handleBpmnError(externalTask, CustomerServiceTopics.BpmnError.CUSTOMER_ERROR,
                    exception.toString());
            externalTaskService.complete(externalTask, null);
        }

        logger.debug("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }
}
