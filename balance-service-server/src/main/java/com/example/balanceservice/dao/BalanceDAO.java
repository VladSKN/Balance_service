package com.example.balanceservice.dao;

import java.util.Optional;

public interface BalanceDAO {
    Optional<Long> getBalance(Long id);

    void changeBalance(Long id, Long amount);
}
