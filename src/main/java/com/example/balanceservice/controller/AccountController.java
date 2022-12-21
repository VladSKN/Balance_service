package com.example.balanceservice.controller;


import com.example.balanceservice.exception.IdNotFoundException;
import com.example.balanceservice.to.AccountTO;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.example.balanceservice.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Tag(name = "AccountController", description = "account balance")
@RequestMapping(value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final BalanceService balanceService;

    public AccountController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/getBalance/{accountId}")
    @Operation(summary = "getBalanceById")
    public Optional<Long> getBalanceById(@Parameter(description = "accountId") @PathVariable Long accountId)
            throws NotEnoughFundsException, IdNotFoundException {
        log.info("getAccountById by AccountController start method");
        long startTime = System.currentTimeMillis();
        Optional<Long> balance = balanceService.getBalance(accountId);
        long endTime = System.currentTimeMillis();
        log.info("getAccountById by AccountController successfully, accountId = {}, balance = {}, time elapsed {} ms",
                accountId, balance.orElseThrow(() ->
                        new NotEnoughFundsException("Account with id " + accountId + " does not have enough founds")),
                (endTime - startTime));
        return balance;
    }

    @PostMapping(value = "/changeBalance/")
    @Operation(summary = "changeBalanceById")
    public String changeBalanceById(@Parameter(description = "account") @RequestBody AccountTO account)
            throws NotEnoughFundsException, IdNotFoundException {
        log.info("changeBalanceById by AccountController start method");
        long startTime = System.currentTimeMillis();
        balanceService.changeBalance(account.getId(), account.getAmount());
        long endTime = System.currentTimeMillis();
        log.info("changeBalanceById by AccountController successfully, accountId = {}, balance = {}, time elapsed {} ms",
                account.getId(), account.getAmount(), (endTime - startTime));
        return String.format("changeBalance successfully accountId = %d", account.getId());
    }

    @ExceptionHandler(NotEnoughFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNoSuchElementFoundException(NotEnoughFundsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(IdNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
