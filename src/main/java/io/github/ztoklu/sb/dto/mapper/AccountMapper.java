package io.github.ztoklu.sb.dto.mapper;

import io.github.ztoklu.sb.dto.AccountDTO;
import io.github.ztoklu.sb.dto.AccountWithTransactionsDTO;
import io.github.ztoklu.sb.dto.TransactionDTO;
import io.github.ztoklu.sb.entity.Account;
import io.github.ztoklu.sb.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountDTO mapAccountToDto(Account account);

    TransactionDTO mapTransactionToDto(Transaction transaction);

    AccountWithTransactionsDTO mapAccountWithTransactionsToDto(Account account);

}
