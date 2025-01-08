package io.github.ztoklu.sb.service.impl.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.entity.*;
import io.github.ztoklu.sb.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneBillTransactionCommand extends TransactionCommand {

    @Autowired
    public PhoneBillTransactionCommand(TransactionRepository transactionRepository,
                                       ObjectMapper objectMapper) {
        super(transactionRepository, objectMapper);
    }

    @Override
    public TransactionCommandType getType() {
        return TransactionCommandType.PHONE_BILL;
    }

    @Override
    public TransactionCommandResult execute(Account account, TransactionRequestInputDTO transactionRequest) {
        checkIfAmountGreaterThanZero(transactionRequest);
        checkIfAccountHasEnoughBalance(account.getBalance(), transactionRequest.getAmount());

        account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(TransactionType.DEBIT);
        transaction.setSubType(TransactionSubType.PHONE_BILL);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAccount(account);
        transaction.setAdditionalJsonData(convertMapToJson(transactionRequest.getAdditionalData()));
        getTransactionRepository().save(transaction);

        TransactionCommandResult transactionCommandResult = new TransactionCommandResult();
        transactionCommandResult.setTransaction(transaction);
        transactionCommandResult.setSuccess(true);
        return transactionCommandResult;
    }

}
