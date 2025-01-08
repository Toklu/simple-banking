package io.github.ztoklu.sb.service.impl.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.entity.Account;
import io.github.ztoklu.sb.entity.Transaction;
import io.github.ztoklu.sb.entity.TransactionStatus;
import io.github.ztoklu.sb.entity.TransactionType;
import io.github.ztoklu.sb.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditTransactionCommand extends TransactionCommand {

    @Autowired
    public CreditTransactionCommand(TransactionRepository transactionRepository,
                                    ObjectMapper objectMapper) {
        super(transactionRepository,objectMapper);
    }

    @Override
    public TransactionCommandType getType() {
        return TransactionCommandType.CREDIT;
    }

    @Override
    public TransactionCommandResult execute(Account account, TransactionRequestInputDTO transactionRequest) {
        checkIfAmountGreaterThanZero(transactionRequest);

        account.setBalance(account.getBalance().add(transactionRequest.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAccount(account);
        getTransactionRepository().save(transaction);

        TransactionCommandResult transactionCommandResult = new TransactionCommandResult();
        transactionCommandResult.setTransaction(transaction);
        transactionCommandResult.setSuccess(true);
        return transactionCommandResult;
    }

}
