package com.example.balanceservice.dao.impl;


import com.example.balanceservice.dao.BalanceDAO;
import com.example.balanceservice.entity.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class BalanceDAOPostgresImpl implements BalanceDAO {

    private static final Logger log = LoggerFactory.getLogger(BalanceDAOPostgresImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private static final AccountRowMapper accountRowMapper = new AccountRowMapper();

    public BalanceDAOPostgresImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        Optional<Long> getBalance;
        final String GET_ACCOUNT_SQL = "SELECT * FROM balance_service.account WHERE id = ?";
        List<AccountEntity> query = jdbcTemplate.query(GET_ACCOUNT_SQL, accountRowMapper, id);
        getBalance = query.stream()
                .map(AccountEntity::getAmount)
                .findAny();
        if (getBalance.isEmpty()) {
            log.error("getBalance from BalanceDAO balance empty");
            return Optional.empty();
        }
        log.info("getBalance from BalanceDAO successfully: {}", getBalance);
        return getBalance;
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        final String CHANGE_BALANCE_SQL = "UPDATE balance_service.account SET AMOUNT = ? WHERE ID = ?";
        Optional<Long> balance = getBalance(id);
        if (balance.isPresent()) {
            jdbcTemplate.update(CHANGE_BALANCE_SQL, (balance.get() + amount), id);
            log.info("changeBalanceById by BalanceServiceImpl id = {}, amount = {} change amount = {}"
                    , id, amount, balance.get());
        }
    }

    private static class AccountRowMapper implements RowMapper<AccountEntity> {
        @Override
        public AccountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AccountEntity(rs.getLong("ID"), rs.getLong("AMOUNT"));
        }
    }
}
