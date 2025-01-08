package io.github.ztoklu.sb.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "tbl_account")
public class Account extends BaseEntity<String> {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "account_number", nullable = false, updatable = false, unique = true)
    private String accountNumber;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public String getId() {
        return this.accountNumber;
    }

}
