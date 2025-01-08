package io.github.ztoklu.sb;

import io.github.ztoklu.sb.dto.*;
import io.github.ztoklu.sb.entity.TransactionStatus;
import io.github.ztoklu.sb.entity.TransactionSubType;
import io.github.ztoklu.sb.entity.TransactionType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void findAccountByAccountNumberShouldReturnAnExistingAccount() {
        ApiResponseDTO<AccountDTO> response = this.restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/accounts/12345",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ApiResponseDTO<AccountDTO>>() {
                        })
                .getBody();

        assertTrue(response.isSuccess());
        assertThat(response.getData().getOwner()).isEqualTo("Zeynep Toklu");
    }

    @Test
    @Order(2)
    void findAccountByAccountNumberShouldReturn404IfAccountNotFound() {
        ResponseEntity<ApiResponseDTO<AccountDTO>> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/accounts/123456",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDTO<AccountDTO>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("The account 123456 not found.");
    }

    @Test
    @Order(3)
    void doCreditShouldReturnSuccessfulResult() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        RequestEntity<Map<String, Object>> request = RequestEntity
                .post(URI.create("http://localhost:" + port + "/api/v1/accounts/12345/credit"))
                .body(requestBody);

        ApiResponseDTO<TransactionResultDTO> response = this.restTemplate.exchange(request,
                new ParameterizedTypeReference<ApiResponseDTO<TransactionResultDTO>>() {
                }).getBody();

        assertThat(response.getData().isSuccess()).isEqualTo(true);
    }

    @Test
    @Order(4)
    void doDebitShouldReturnSuccessfulResult() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        RequestEntity<Map<String, Object>> request = RequestEntity
                .post(URI.create("http://localhost:" + port + "/api/v1/accounts/12345/debit"))
                .body(requestBody);

        ApiResponseDTO<TransactionResultDTO> response = this.restTemplate.exchange(request,
                new ParameterizedTypeReference<ApiResponseDTO<TransactionResultDTO>>() {
                }).getBody();

        assertThat(response.getData().isSuccess()).isEqualTo(true);
    }

    @Test
    @Order(5)
    void doDebitShouldReturnInsufficientBalanceException() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 50000.0);

        RequestEntity<Map<String, Object>> request = RequestEntity
                .post(URI.create("http://localhost:" + port + "/api/v1/accounts/12345/debit"))
                .body(requestBody);

        ApiResponseDTO<TransactionResultDTO> response = this.restTemplate.exchange(request,
                new ParameterizedTypeReference<ApiResponseDTO<TransactionResultDTO>>() {
                }).getBody();

        assertThat(response.getMessage()).isEqualTo("Insufficient balance!");
    }

    @Test
    @Order(6)
    void doPaymentForPhoneBillShouldReturnSuccessfulResult() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 94.57);
        requestBody.put("paymentType", "PHONE_BILL");
        requestBody.put("carrier", "Vodafone");
        requestBody.put("phoneNumber", "5554443322");

        RequestEntity<Map<String, Object>> request = RequestEntity
                .post(URI.create("http://localhost:" + port + "/api/v1/accounts/12345/do-payment"))
                .body(requestBody);

        ApiResponseDTO<TransactionResultDTO> response = this.restTemplate.exchange(request,
                new ParameterizedTypeReference<ApiResponseDTO<TransactionResultDTO>>() {
                }).getBody();

        assertThat(response.getData().isSuccess()).isEqualTo(true);
    }

    @Test
    @Order(7)
    void doPaymentForElectricBillShouldReturnSuccessfulResult() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 487.89);
        requestBody.put("paymentType", "ELECTRIC_BILL");
        requestBody.put("distributionCompany", "EnerjiSA");
        requestBody.put("customerNo", "1234567890");

        RequestEntity<Map<String, Object>> request = RequestEntity
                .post(URI.create("http://localhost:" + port + "/api/v1/accounts/12345/do-payment"))
                .body(requestBody);

        ApiResponseDTO<TransactionResultDTO> response = this.restTemplate.exchange(request,
                new ParameterizedTypeReference<ApiResponseDTO<TransactionResultDTO>>() {
                }).getBody();

        assertThat(response.getData().isSuccess()).isEqualTo(true);
    }

    @Test
    @Order(8)
    void findAccountByAccountNumberShouldReturnAnExistingAccountWithTransactions() {
        AccountWithTransactionsDTO response = this.restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/accounts/12345?includeTransactions=true",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ApiResponseDTO<AccountWithTransactionsDTO>>() {
                        })
                .getBody()
                .getData();

        List<TransactionDTO> transactionDTOS = response.getTransactions();

        TransactionDTO creditTransaction = transactionDTOS.stream()
                .filter(x -> TransactionType.CREDIT.equals(x.getType())).findFirst().get();

        TransactionDTO debitTransaction = transactionDTOS.stream()
                .filter(x -> TransactionType.DEBIT.equals(x.getType()) && x.getSubType() == null).findFirst().get();

        TransactionDTO electricBillPaymentTransaction = transactionDTOS.stream()
                .filter(x -> TransactionSubType.ELECTRIC_BILL.equals(x.getSubType())).findFirst().get();

        TransactionDTO phoneBillPaymentTransaction = transactionDTOS.stream()
                .filter(x -> TransactionSubType.PHONE_BILL.equals(x.getSubType())).findFirst().get();

        assertThat(transactionDTOS.size()).isEqualTo(4);
        assertThat(creditTransaction.getAmount().compareTo(BigDecimal.valueOf(500.0))).isEqualTo(0);
        assertThat(debitTransaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(phoneBillPaymentTransaction.getAdditionalJsonData()).contains("carrier");
        assertThat(electricBillPaymentTransaction.getAdditionalJsonData()).contains("customerNo");
    }


}
