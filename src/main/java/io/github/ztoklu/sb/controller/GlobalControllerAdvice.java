package io.github.ztoklu.sb.controller;

import io.github.ztoklu.sb.dto.ApiResponseDTO;
import io.github.ztoklu.sb.exceptions.AccountNotFoundException;
import io.github.ztoklu.sb.exceptions.InsufficientBalanceException;
import io.github.ztoklu.sb.exceptions.InvalidDataInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseDTO<?>> handleAccountNotFound(AccountNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        ApiResponseDTO<?> dto = ApiResponseDTO.fromError(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler({InsufficientBalanceException.class, InvalidDataInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<?>> handleInsufficientBalanceAndInvalidDataInput(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ApiResponseDTO<?> dto = ApiResponseDTO.fromError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<?>> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponseDTO<?> dto = ApiResponseDTO.fromError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

}
