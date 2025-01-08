package io.github.ztoklu.sb.service;

import io.github.ztoklu.sb.dto.AccountDTO;
import io.github.ztoklu.sb.dto.AccountWithTransactionsDTO;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.dto.TransactionResultDTO;
import io.github.ztoklu.sb.entity.Account;

public interface AccountService {

    AccountWithTransactionsDTO findAccountWithTransactionsByNumber(String accountNumber);

    AccountDTO findAccountDTOByNumber(String accountNumber);

    Account findAccountByNumberOrElseThrow(String accountNumber);

    TransactionResultDTO executeTransaction(TransactionRequestInputDTO transactionRequest);

}

