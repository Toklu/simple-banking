package io.github.ztoklu.sb.controller;

import io.github.ztoklu.sb.dto.ApiResponseDTO;
import io.github.ztoklu.sb.dto.TransactionRequestInputDTO;
import io.github.ztoklu.sb.dto.TransactionResultDTO;
import io.github.ztoklu.sb.exceptions.InvalidDataInputException;
import io.github.ztoklu.sb.service.AccountService;
import io.github.ztoklu.sb.service.impl.commands.TransactionCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/accounts")
class AccountController {

    private final AccountService accountService;

    @Autowired
    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponseDTO<?>> findAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber,
                                                                        @RequestParam(value = "includeTransactions", required = false, defaultValue = "false") boolean includeTransactions) {
        Serializable response;
        if (includeTransactions) {
            response = accountService.findAccountWithTransactionsByNumber(accountNumber);
        } else {
            response = accountService.findAccountDTOByNumber(accountNumber);
        }
        return ResponseEntity.ok(ApiResponseDTO.fromData(response));
    }

    @PostMapping("/{accountNumber}/credit")
    public ResponseEntity<ApiResponseDTO<TransactionResultDTO>> doCredit(@PathVariable("accountNumber") String accountNumber,
                                                                         @RequestBody Map<String, Object> requestBody) {
        return doCreditOrDebit(TransactionCommandType.CREDIT, accountNumber, requestBody);
    }

    @PostMapping("/{accountNumber}/debit")
    public ResponseEntity<ApiResponseDTO<TransactionResultDTO>> doDebit(@PathVariable("accountNumber") String accountNumber,
                                                                        @RequestBody Map<String, Object> requestBody) {
        return doCreditOrDebit(TransactionCommandType.DEBIT, accountNumber, requestBody);
    }

    private ResponseEntity<ApiResponseDTO<TransactionResultDTO>> doCreditOrDebit(
            TransactionCommandType type, String accountNumber, Map<String, Object> requestBody) {
        checkBodyContains(requestBody, "amount");
        TransactionRequestInputDTO input = new TransactionRequestInputDTO();
        input.setAccountNumber(accountNumber);
        input.setCommandType(type);
        input.setAmount(BigDecimal.valueOf((Double) requestBody.get("amount")));
        return ResponseEntity.ok(ApiResponseDTO.fromData(accountService.executeTransaction(input)));
    }

    private void checkBodyContains(Map<String, Object> requestBody, String key) {
        if (CollectionUtils.isEmpty(requestBody) || requestBody.get(key) == null) {
            throw new InvalidDataInputException("You must provide " + key + " in your request.");
        }
    }

    @PostMapping("/{accountNumber}/do-payment")
    public ResponseEntity<ApiResponseDTO<TransactionResultDTO>> doBillPayment(@PathVariable("accountNumber") String accountNumber,
                                                                              @RequestBody Map<String, Object> requestBody) {
        checkBodyContains(requestBody, "amount");
        checkBodyContains(requestBody, "paymentType");

        TransactionRequestInputDTO input = new TransactionRequestInputDTO();
        input.setAccountNumber(accountNumber);
        input.setCommandType(TransactionCommandType.valueOf((String) requestBody.get("paymentType")));
        input.setAmount(BigDecimal.valueOf((Double) requestBody.get("amount")));

        requestBody.remove("amount");
        requestBody.remove("paymentType");
        input.setAdditionalData(requestBody);

        return ResponseEntity.ok(ApiResponseDTO.fromData(accountService.executeTransaction(input)));
    }

}
