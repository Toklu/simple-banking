package io.github.ztoklu.sb.dto;

import io.github.ztoklu.sb.entity.TransactionStatus;
import io.github.ztoklu.sb.entity.TransactionSubType;
import io.github.ztoklu.sb.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO implements Serializable {

    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionSubType subType;
    private TransactionStatus status;
    private String additionalJsonData;
    private LocalDateTime createdDate;

}
