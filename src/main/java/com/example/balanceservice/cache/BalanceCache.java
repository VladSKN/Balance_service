package com.example.balanceservice.cache;

import com.example.balanceservice.dao.BalanceDAO;
import com.example.balanceservice.exception.NotEnoughFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class BalanceCache {

    private final Map<Long, Optional<Long>> balanceCache = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(BalanceCache.class);

    private final BalanceDAO balanceDAO;

    public BalanceCache(BalanceDAO balanceDAO) {
        this.balanceDAO = balanceDAO;
    }

    public Optional<Long> getBalance(Long id) {
        Optional<Long> cached = balanceCache.get(id);
        if (cached == null) { // почему cached возвращает null если это Optional???
            Optional<Long> balance = balanceDAO.getBalance(id);
            balanceCache.put(id, balance);
            log.info("");
            return balance;
        } else {
            log.info("");
            return cached;
        }
    }

    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        balanceDAO.changeBalance(id, amount);
        balanceCache.computeIfPresent(id, (key, value) -> balanceDAO.getBalance(id));
        log.info("");
    }
}
