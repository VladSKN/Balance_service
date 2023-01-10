package com.example.balanceservice.service.impl;

import com.example.balanceservice.cache.NoBlockingBalanceCache;
import com.example.balanceservice.exception.IdNotFoundException;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.example.balanceservice.service.BalanceService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Service
public class BalanceServiceImpl implements BalanceService {

    private final CacheLoader<Long, ReentrantReadWriteLock> loader = new CacheLoader<>() {
        @Override
        public ReentrantReadWriteLock load(Long aLong) {
            return new ReentrantReadWriteLock();
        }
    };

    private final LoadingCache<Long, ReentrantReadWriteLock> cacheAccountIdToLock = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(loader);

    private final NoBlockingBalanceCache balanceCache;

    private static final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    public BalanceServiceImpl(NoBlockingBalanceCache balanceCache) {
        this.balanceCache = balanceCache;
    }

    @Override
    public Optional<Long> getBalance(Long id) throws IdNotFoundException {
        Optional<Long> getBalance;
        cacheAccountIdToLock.put(id, new ReentrantReadWriteLock());
        try {
            cacheAccountIdToLock.getUnchecked(id).readLock().lock();
            getBalance = balanceCache.getBalance(id);
            if (getBalance.isEmpty()) {
                throw new IdNotFoundException("id not found");
            }
            log.info("getBalance by BalanceServiceImpl successfully");
        } catch (IdNotFoundException e) {
            throw new IdNotFoundException(e.getMessage());
        } finally {
            cacheAccountIdToLock.getUnchecked(id).readLock().unlock();
        }
        return getBalance;
    }

    @Override
    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException, IdNotFoundException {
        cacheAccountIdToLock.put(id, new ReentrantReadWriteLock());
        try {
            cacheAccountIdToLock.getUnchecked(id).writeLock().lock();
            Optional<Long> balance = balanceCache.getBalance(id);
            if (balance.isEmpty()) {
                throw new IdNotFoundException("Account with id " + id + " not found");
            } else {
                if (balance.get() + amount >= 0) {
                    balanceCache.changeBalance(id, amount);
                } else {
                    log.error("not enough money");
                    throw new NotEnoughFundsException("not enough money");
                }
            }
        } finally {
            cacheAccountIdToLock.getUnchecked(id).writeLock().unlock();
        }
        log.info("changeBalance by BalanceServiceImpl successfully");
    }
}
