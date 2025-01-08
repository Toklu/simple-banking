package io.github.ztoklu.sb;

import io.github.ztoklu.sb.entity.Account;
import io.github.ztoklu.sb.exceptions.AccountNotFoundException;
import io.github.ztoklu.sb.repository.AccountRepository;
import io.github.ztoklu.sb.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
public class AccountServiceImplUnitTests {

    @MockitoBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    void findAccountByNumberShouldReturnAccountIfExists() {
        Account mockAccount = new Account();
        mockAccount.setAccountNumber("123");
        mockAccount.setOwner("ztoklu");
        mockAccount.setBalance(BigDecimal.valueOf(1000.0));

        Mockito.when(accountRepository.findById("123")).thenReturn(Optional.of(mockAccount));

        Account foundAccount = accountService.findAccountByNumberOrElseThrow("123");

        assertEquals(foundAccount.getOwner(), "ztoklu");

        Mockito.verify(accountRepository, Mockito.times(1)).findById("123");
    }

    @Test
    void findAccountByNumberShouldThrowAccountNotFoundExceptionIfAccountNotFound() {
        Mockito.when(accountRepository.findById("1234")).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrowsExactly(AccountNotFoundException.class, () -> {
            accountService.findAccountByNumberOrElseThrow("1234");
        });

        Assertions.assertEquals("The account 1234 not found.", exception.getMessage());

        Mockito.verify(accountRepository, Mockito.times(1)).findById("1234");
    }

}
