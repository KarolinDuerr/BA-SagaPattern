package saga.camunda.customerservice.api;

public class CustomerServiceTopics {

    public static class DataInput {

        public static final String VALIDATE_CUSTOMER_DATA = "customerId";

    }

    public static class BpmnError {

        public static final String CUSTOMER_ERROR = "CUSTOMER_ERROR";

    }
}
