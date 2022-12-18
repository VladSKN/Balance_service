package com.example.balanceservice.service;

import com.example.balanceservice.exception.NotEnoughFundsException;

import java.util.Optional;

public interface BalanceService {
    /**
     * Получение баланса
     *
     * @param id идентификатор банковского счёта
     * @return сумма денег на банковском счёте
     */
    Optional<Long> getBalance(Long id) throws NotEnoughFundsException;

    /**
     * Изменение баланса на определённое значение
     *
     * @param id    идентификатор банковского счёта
     * @param value сумма денег, которую нужно добавить к банковскому счёту
     */
    void changeBalance(Long id, Long amount) throws NotEnoughFundsException;
}
