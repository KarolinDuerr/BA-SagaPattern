package saga.microprofile.customerservice.api.dto;

public class ValidateCustomerRequest {

    private Long customerId;

    // default constructor necessary for Eventuate Framework
    public ValidateCustomerRequest() {
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
