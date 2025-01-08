package io.github.ztoklu.sb.dto;

import io.github.ztoklu.sb.service.impl.commands.TransactionCommandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestInputDTO {

    private String accountNumber;
    private TransactionCommandType commandType;
    private BigDecimal amount;
    private Map<String, Object> additionalData;

}
