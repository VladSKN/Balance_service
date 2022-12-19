package com.example.balanceservice.service.impl;

import com.example.balanceservice.cache.BalanceCache;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.example.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Service
public class BalanceServiceImpl implements BalanceService {

    private final Map<Long, ReentrantReadWriteLock> accountIdToLock = new HashMap<>();

    private final BalanceCache balanceCache;

    private static final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    public BalanceServiceImpl(BalanceCache balanceCache) {
        this.balanceCache = balanceCache;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        Optional<Long> getBalance;
        accountIdToLock.putIfAbsent(id, new ReentrantReadWriteLock());
        try {
            accountIdToLock.get(id).readLock().lock();
            getBalance = balanceCache.getBalance(id);
            log.info("");
        } finally {
            accountIdToLock.get(id).readLock().unlock();
        }
        return getBalance;
    }

    @Override
    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        accountIdToLock.putIfAbsent(id, new ReentrantReadWriteLock());
        try {
            accountIdToLock.get(id).writeLock().lock();
            balanceCache.changeBalance(id, amount);
        } finally {
            accountIdToLock.get(id).writeLock().unlock();
        }
        log.info("");
    }
}
