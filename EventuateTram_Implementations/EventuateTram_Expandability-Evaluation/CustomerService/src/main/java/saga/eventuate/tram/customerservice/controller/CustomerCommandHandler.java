package saga.eventuate.tram.customerservice.controller;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.eventuate.tram.customerservice.api.CustomerServiceChannels;
import saga.eventuate.tram.customerservice.api.dto.CustomerValidationFailed;
import saga.eventuate.tram.customerservice.api.dto.ValidateCustomerCommand;
import saga.eventuate.tram.customerservice.error.CustomerServiceException;
import saga.eventuate.tram.customerservice.error.ErrorType;

public class CustomerCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerCommandHandler.class);

    @Autowired
    private final ICustomerService customerService;

    public CustomerCommandHandler(final ICustomerService customerService) {
        this.customerService = customerService;
    }

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(CustomerServiceChannels.customerServiceChannel)
                .onMessage(ValidateCustomerCommand.class, this::validateCustomer)
                .build();
    }

    private Message validateCustomer(CommandMessage<ValidateCustomerCommand> command) {
        final ValidateCustomerCommand validateCustomerCommand = command.getCommand();
        logger.info("Received ValidateCustomerCommand: " + validateCustomerCommand);

        try {
            customerService.validateCustomer(validateCustomerCommand.getCustomerId());
            return CommandHandlerReplyBuilder.withSuccess();
        } catch (CustomerServiceException exception) {
            logger.error(exception.toString());

            if (exception.getErrorType() == ErrorType.CUSTOMER_VALIDATION_FAILED) {
                return CommandHandlerReplyBuilder.withFailure(new CustomerValidationFailed());
            }

            return CommandHandlerReplyBuilder.withFailure(exception.toString());
        }
    }
}
