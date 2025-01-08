package io.github.ztoklu.sb.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "tbl_transaction")
public class Transaction extends BaseEntity<Long> {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_transaction_id")
    @SequenceGenerator(name = "seq_tbl_transaction_id", sequenceName = "seq_tbl_transaction_id", allocationSize = 1)
    private Long id;

    @Column(name = "amount", nullable = false, updatable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_code", nullable = false, updatable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type_code", updatable = false)
    private TransactionSubType subType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false)
    private TransactionStatus status;

    @Column(name = "additional_json_data", columnDefinition = "text")
    private String additionalJsonData;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", nullable = false, updatable = false)
    private Account account;

}
