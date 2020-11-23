package saga.netflix.conductor.customerservice.api;

public class CustomerServiceTasks {

    public static class Task {

        public static final String VALIDATE_CUSTOMER = "validateCustomer";
    }

    public static class TaskInput {

        public static final String VALIDATE_CUSTOMER_INPUT = "validateCustomerRequest";

    }
}
