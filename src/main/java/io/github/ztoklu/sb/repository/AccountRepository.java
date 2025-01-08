package io.github.ztoklu.sb.repository;

import io.github.ztoklu.sb.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("""
            SELECT a
            FROM Account a
            LEFT JOIN FETCH a.transactions t
            WHERE a.accountNumber = ?1
            """)
    Optional<Account> findByAccountNumberFetchTransactions(String accountNumber);

    @Query("""
            SELECT a
            FROM Account a
            WHERE a.accountNumber = ?1
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountNumberAndAcquireLock(String accountNumber);
}
