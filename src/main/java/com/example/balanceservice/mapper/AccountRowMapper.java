package com.example.balanceservice.mapper;

import com.example.balanceservice.entity.AccountEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<AccountEntity> {
    @Override
    public AccountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountEntity account = new AccountEntity();
        account.setId(rs.getLong("ID"));
        account.setAmount(rs.getLong("AMOUNT"));
        return account;
    }
}
