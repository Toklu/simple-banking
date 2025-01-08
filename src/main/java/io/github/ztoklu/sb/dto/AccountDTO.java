package io.github.ztoklu.sb.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountDTO implements Serializable {

    private String accountNumber;
    private String owner;
    private BigDecimal balance;
    private LocalDateTime createdDate;

}
