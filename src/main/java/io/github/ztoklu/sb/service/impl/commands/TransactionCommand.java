package io.github.ztoklu.sb.service.impl.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.entity.Account;
import io.github.ztoklu.sb.exceptions.InsufficientBalanceException;
import io.github.ztoklu.sb.exceptions.InvalidDataInputException;
import io.github.ztoklu.sb.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
public abstract class TransactionCommand {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public TransactionCommand(TransactionRepository transactionRepository,
                              ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    protected final TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    protected final ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void checkIfAmountGreaterThanZero(TransactionRequestInputDTO transactionRequest) {
        if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataInputException("Amount must be greater than zero.");
        }
    }

    protected void checkIfAccountHasEnoughBalance(BigDecimal currentBalance, BigDecimal amount) {
        BigDecimal amountLeftAfterTransaction = currentBalance.subtract(amount);
        if (amountLeftAfterTransaction.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance!");
        }
    }

    protected String convertMapToJson(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public abstract TransactionCommandType getType();

    public abstract TransactionCommandResult execute(Account account, TransactionRequestInputDTO transactionRequest);

}
