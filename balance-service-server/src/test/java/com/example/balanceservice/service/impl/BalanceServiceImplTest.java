package com.example.balanceservice.service.impl;

import com.example.balanceservice.cache.NoBlockingBalanceCache;
import com.example.balanceservice.exception.IdNotFoundException;
import com.example.balanceservice.exception.NotEnoughFundsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.example.balanceservice.service.impl.BalanceServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class BalanceServiceImplTest {

    @InjectMocks
    BalanceServiceImpl balanceService;

    @Mock
    NoBlockingBalanceCache balanceCacheMock;


    @Test
    @DisplayName("Тестирование запроса баланса, должно пройти успешно")
    void getBalance() throws IdNotFoundException {
        //given
        Optional<Long> testBalance = Optional.of(10L);

        //when
        when(balanceCacheMock.getBalance(1L)).thenReturn(testBalance);

        //then
        Optional<Long> balance = balanceService.getBalance(1L);
        assertEquals(balance.get(), testBalance.get());
        verify(balanceCacheMock).getBalance(1L);
    }

    @Test
    @DisplayName("Тестирование изменения баланса, должно пройти успешно")
    void changeBalance() throws NotEnoughFundsException, IdNotFoundException {
        //given

        //when
        when(balanceCacheMock.getBalance(1L)).thenReturn(Optional.of(1L));

        //then
        doAnswer(invocation -> {
            long id = invocation.getArgument(1);
            long amount = invocation.getArgument(1);
            assertEquals(1L, id);
            assertEquals(1L, amount);

            return null;
        }).when(balanceCacheMock).changeBalance(1L, 1L);

        balanceService.changeBalance(1L, 1L);

        verify(balanceCacheMock).changeBalance(1L, 1L);
    }
}