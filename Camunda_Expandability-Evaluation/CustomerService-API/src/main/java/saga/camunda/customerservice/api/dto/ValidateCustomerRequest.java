package saga.camunda.customerservice.api.dto;

import java.io.Serializable;

public class ValidateCustomerRequest implements Serializable {

    private Long customerId;

    // default constructor necessary for Eventuate Framework
    private ValidateCustomerRequest() {
    }

    public ValidateCustomerRequest(final Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final Long customerId) {
        this.customerId = customerId;
    }
}
