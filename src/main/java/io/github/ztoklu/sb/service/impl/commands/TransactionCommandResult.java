package io.github.ztoklu.sb.service.impl.commands;

import io.github.ztoklu.sb.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionCommandResult implements Serializable {

    private Transaction transaction;
    private boolean success;

}
