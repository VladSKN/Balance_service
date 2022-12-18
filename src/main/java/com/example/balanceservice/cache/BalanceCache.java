package com.example.balanceservice.cache;

import com.example.balanceservice.dao.BalanceDAO;
import com.example.balanceservice.exception.NotEnoughFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Component
public class BalanceCache {
    private final Map<Long, ReentrantReadWriteLock> accountIdToLock = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(BalanceCache.class);

    private final BalanceDAO balanceDAO;

    public BalanceCache(BalanceDAO balanceDAO) {
        this.balanceDAO = balanceDAO;
    }

    public Optional<Long> getBalance(Long id) {
        accountIdToLock.putIfAbsent(id, new ReentrantReadWriteLock());
        Optional<Long> getBalance;
        try {
            accountIdToLock.get(id).readLock().lock();
            getBalance = balanceDAO.getBalance(id);
            log.info("");
        } finally {
            accountIdToLock.get(id).readLock().unlock();
        }
        return getBalance;
    }

    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        accountIdToLock.putIfAbsent(id, new ReentrantReadWriteLock());
        try {
            accountIdToLock.get(id).writeLock().lock();
            balanceDAO.changeBalance(id, amount);
        } finally {
            accountIdToLock.get(id).writeLock().unlock();
        }
    }
}
