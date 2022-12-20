package com.example.balanceservice.cache;


import com.example.balanceservice.dao.BalanceDAO;
import com.example.balanceservice.exception.NotEnoughFundsException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Component
public class BalanceCache {

    private final CacheLoader<Long, Optional<Long>> loader = new CacheLoader<>() {
        @Override
        public Optional<Long> load(Long aLong) {
            return balanceDAO.getBalance(aLong);
        }
    };

    private final LoadingCache<Long, Optional<Long>> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(loader);

    private static final Logger log = LoggerFactory.getLogger(BalanceCache.class);

    private final BalanceDAO balanceDAO;

    public BalanceCache(BalanceDAO balanceDAO) {
        this.balanceDAO = balanceDAO;
    }

    public Optional<Long> getBalance(Long id) {
        Optional<Long> cached = cache.getUnchecked(id);
        log.info("getBalance by BalanceCache successfully {}", cached);
        return cached;
    }

    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        balanceDAO.changeBalance(id, amount);
        cache.refresh(id);
        log.info("changeBalance by BalanceCache successfully");
    }
}
