package com.example.balanceservice.service;

import com.example.balanceservice.entity.AccountEntity;

import java.util.Optional;

public interface BalanceService {
    /**
     * Получение баланса
     *
     * @param id идентификатор банковского счёта
     * @return сумма денег на банковском счёте
     */
    Optional<Long> getBalance(Long id);

    /**
     * Изменение баланса на определённое значение
     *
     * @param id    идентификатор банковского счёта
     * @param value сумма денег, которую нужно добавить к банковскому счёту
     */
    void changeBalance(Long id, Long amount);

    Optional<AccountEntity> getAccountById(Long id);
}
