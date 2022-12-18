package com.example.balanceservice.service.impl;

import com.example.balanceservice.cache.BalanceCache;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.example.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO Возможно тут нужен DAO?
// TODO предусмотреть кэш
// TODO синхронизация в Service?

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceCache balanceCache;

    private static final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    public BalanceServiceImpl(BalanceCache balanceCache) {
        this.balanceCache = balanceCache;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        log.info("");
        return balanceCache.getBalance(id);
    }

    @Override
    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        log.info("");
        balanceCache.changeBalance(id, amount);
    }
}
