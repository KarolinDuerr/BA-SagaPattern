package saga.microprofile.customerservice.api.dto;

public class ValidateCustomerCommand {

    private Long customerId;

    // default constructor necessary for Eventuate Framework
    public ValidateCustomerCommand() {
    }

    public ValidateCustomerCommand(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
