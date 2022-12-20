package com.example.balanceservice.dao;


import com.example.balanceservice.entity.AccountEntity;
import com.example.balanceservice.exception.NotEnoughFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class BalanceDAO {

    private static final Logger log = LoggerFactory.getLogger(BalanceDAO.class);

    private final JdbcTemplate jdbcTemplate;

    private static final AccountRowMapper accountRowMapper = new AccountRowMapper();

    public BalanceDAO(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> getBalance(Long id) {
        Optional<Long> getBalance;
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
        return getBalance;
    }

    public void changeBalance(Long id, Long amount) throws NotEnoughFundsException {
        String errorIdNotFound = "Account with id " + id + " does not have enough founds";
        try {
            final String GET_ACCOUNT_SQL = "SELECT * FROM balance_service.account WHERE id = ?";
            AccountEntity account = jdbcTemplate.queryForObject(GET_ACCOUNT_SQL, accountRowMapper, id);
            if (account == null) {
                throw new NotEnoughFundsException(errorIdNotFound);
            }
            account.setAmount(account.getAmount() + amount);
            final String CHANGE_BALANCE_SQL = "UPDATE balance_service.account SET AMOUNT = ? WHERE ID = ?";
            jdbcTemplate.update(CHANGE_BALANCE_SQL, account.getAmount(), account.getId());
            log.info("changeBalanceById by BalanceServiceImpl id = {}, amount = {} change amount = {}"
                    , account.getId(), amount, account.getAmount());
        } catch (EmptyResultDataAccessException e) {
            log.error(errorIdNotFound);
            throw new NotEnoughFundsException(errorIdNotFound);
        }
    }


    private static class AccountRowMapper implements RowMapper<AccountEntity> {
        @Override
        public AccountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            AccountEntity account = new AccountEntity();
            account.setId(rs.getLong("ID"));
            account.setAmount(rs.getLong("AMOUNT"));
            return account;
        }
    }
}
