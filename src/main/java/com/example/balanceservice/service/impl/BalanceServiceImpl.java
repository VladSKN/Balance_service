package com.example.balanceservice.service.impl;

import com.example.balanceservice.entity.AccountEntity;
import com.example.balanceservice.mapper.AccountRowMapper;
import com.example.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// TODO Возможно тут нужен DAO?
// TODO предусмотреть кэш

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final AccountRowMapper accountRowMapper = new AccountRowMapper();

    public BalanceServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        Optional<Long> getBalance;
        try {
            lock.readLock().lock();
            final String GET_ACCOUNT_SQL = "SELECT * FROM balance_service.account WHERE id = ?";
            List<AccountEntity> query = jdbcTemplate.query(GET_ACCOUNT_SQL, accountRowMapper, id);
            getBalance = query.stream()
                    .map(AccountEntity::getAmount)
                    .findAny();

            if (getBalance.isEmpty()) {
                log.error("getBalance from BalanceServiceImpl balance empty");
                return Optional.empty();
            }

            log.info("getBalance from BalanceServiceImpl successfully: {}", getBalance);
        } finally {
            lock.readLock().unlock();
        }
        return getBalance;
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        try {
            lock.writeLock().lock();
            final String GET_ACCOUNT_SQL = "SELECT * FROM balance_service.account WHERE id = ?";

            AccountEntity account = jdbcTemplate
                    .queryForObject(GET_ACCOUNT_SQL, accountRowMapper, id);
            if (account == null) {
                log.error("account is null");
                throw new NullPointerException();
            }
            account.setAmount(account.getAmount() + amount);

            final String CHANGE_BALANCE_SQL = "UPDATE balance_service.account SET AMOUNT = ? WHERE ID = ?";

            jdbcTemplate.update(CHANGE_BALANCE_SQL, account.getAmount(), account.getId());
            log.info("changeBalanceById by BalanceServiceImpl id = {}, amount = {} change amount = {}"
                    , account.getId(), amount, account.getAmount());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<AccountEntity> getAccountById(Long id) {
        List<AccountEntity> accountEntities;
        try {
            lock.readLock().lock();
            final String GET_ACCOUNT_SQL = "SELECT * FROM balance_service.account WHERE id = ?";
            accountEntities = jdbcTemplate.query(GET_ACCOUNT_SQL, accountRowMapper, id);
            log.info("getAccountById by BalanceServiceImpl successfully");

        } finally {
            lock.readLock().unlock();
        }
        return accountEntities.stream().findAny();
    }
}
