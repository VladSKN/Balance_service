package com.example.balanceservice.controller;

import com.example.balanceservice.dto.AccountDTO;
import com.example.balanceservice.entity.AccountEntity;
import com.example.balanceservice.mapper.AccountMapper;
import com.example.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final BalanceService balanceService;

    public AccountController(AccountMapper mapper, BalanceService balanceService) {
        this.mapper = mapper;
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/{accountId}")
    public Optional<Long> getBalanceById(@PathVariable Long accountId) {
        Optional<Long> balance = balanceService.getBalance(accountId);
        log.info("getAccountById by AccountController id = {}, amount = {}", accountId, balance.orElse(null));
        return balance;
    }

    @PostMapping
    public AccountDTO changeBalanceById(@RequestBody AccountDTO account) {
        balanceService.changeBalance(account.getId(), account.getAmount());
        AccountEntity accountEntity = balanceService.getAccountById(account.getId()).get();//TODO
        log.info("changeBalanceById by AccountController id = {}, amount = {} change amount = {}",
                account.getId(), account.getAmount(), accountEntity.getAmount());
        return mapper.accountEntityToAccountDTO(accountEntity);
    }
}
