package saga.microProfile.customerservice.controller;

import saga.microprofile.customerservice.api.dto.CustomerValidationFailed;
import saga.microprofile.customerservice.api.dto.ValidateCustomerCommand;
import saga.microProfile.customerservice.error.CustomerServiceException;
import saga.microProfile.customerservice.error.ErrorType;

public class CustomerCommandHandler {


//    private Message validateCustomer(CommandMessage<ValidateCustomerCommand> command) {
//        final ValidateCustomerCommand validateCustomerCommand = command.getCommand();
//        logger.info("Received ValidateCustomerCommand: " + validateCustomerCommand);
//
//        try {
//            customerService.validateCustomer(validateCustomerCommand.getCustomerId());
//            return CommandHandlerReplyBuilder.withSuccess();
//        } catch (CustomerServiceException exception) {
//            logger.error(exception.toString());
//
//            if (exception.getErrorType() == ErrorType.CUSTOMER_VALIDATION_FAILED) {
//                return CommandHandlerReplyBuilder.withFailure(new CustomerValidationFailed());
//            }
//
//            return CommandHandlerReplyBuilder.withFailure(exception.toString());
//        }
//    }
}
