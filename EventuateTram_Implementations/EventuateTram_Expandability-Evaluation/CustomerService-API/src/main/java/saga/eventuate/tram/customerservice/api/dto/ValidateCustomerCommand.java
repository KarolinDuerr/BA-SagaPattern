package saga.eventuate.tram.customerservice.api.dto;

import io.eventuate.tram.commands.common.Command;

public class ValidateCustomerCommand implements Command {

    private Long customerId;

    // default constructor necessary for Eventuate Framework
    private ValidateCustomerCommand() {
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
