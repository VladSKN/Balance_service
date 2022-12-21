package com.example.balanceservice.controller;


import com.example.balanceservice.dto.AccountTO;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.example.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final BalanceService balanceService;

    public AccountController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/getBalance/{accountId}")
    public Optional<Long> getBalanceById(@PathVariable Long accountId) throws NotEnoughFundsException {
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
    public String changeBalanceById(@RequestBody AccountTO account) throws NotEnoughFundsException {
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
    public ResponseEntity<String> handleNoSuchElementFoundException(
            NotEnoughFundsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
