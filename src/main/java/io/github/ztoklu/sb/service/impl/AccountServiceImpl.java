package io.github.ztoklu.sb.service.impl;

import io.github.ztoklu.sb.dto.AccountDTO;
import io.github.ztoklu.sb.dto.AccountWithTransactionsDTO;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.dto.TransactionResultDTO;
import io.github.ztoklu.sb.dto.mapper.AccountMapper;
import io.github.ztoklu.sb.entity.Account;
import io.github.ztoklu.sb.exceptions.AccountNotFoundException;
import io.github.ztoklu.sb.repository.AccountRepository;
import io.github.ztoklu.sb.service.AccountService;
import io.github.ztoklu.sb.service.impl.commands.TransactionCommand;
import io.github.ztoklu.sb.service.impl.commands.TransactionCommandResult;
import io.github.ztoklu.sb.service.impl.commands.TransactionCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final Map<TransactionCommandType, TransactionCommand> transactionCommandMap;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountMapper accountMapper,
                              List<TransactionCommand> transactionCommands) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionCommandMap = Collections.unmodifiableMap(transactionCommands.stream().collect(
                Collectors.toMap(TransactionCommand::getType, Function.identity())));
    }

    @Override
    public AccountWithTransactionsDTO findAccountWithTransactionsByNumber(String accountNumber) {
        return accountMapper.mapAccountWithTransactionsToDto(
                accountRepository.findByAccountNumberFetchTransactions(accountNumber)
                        .orElseThrow(() -> new AccountNotFoundException("The account " + accountNumber + " not found.")));
    }

    @Override
    public AccountDTO findAccountDTOByNumber(String accountNumber) {
        return accountMapper.mapAccountToDto(findAccountByNumberOrElseThrow(accountNumber));
    }

    @Override
    public Account findAccountByNumberOrElseThrow(String accountNumber) {
        return accountRepository.findById(accountNumber).orElseThrow(
                () -> new AccountNotFoundException("The account " + accountNumber + " not found."));
    }

    @Override
    @Transactional
    public TransactionResultDTO executeTransaction(TransactionRequestInputDTO transactionRequest) {
        Account account = accountRepository.findByAccountNumberAndAcquireLock(transactionRequest.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("The account " + transactionRequest.getAccountNumber() + " not found."));

        TransactionCommandResult commandResult = transactionCommandMap.get(
                transactionRequest.getCommandType()).execute(account, transactionRequest);

        accountRepository.save(account);

        return new TransactionResultDTO(commandResult.isSuccess(), account.getBalance());
    }

}
