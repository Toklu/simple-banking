package io.github.ztoklu.sb.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AccountWithTransactionsDTO extends AccountDTO implements Serializable {

    private List<TransactionDTO> transactions;

}
