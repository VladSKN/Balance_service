package com.example.balanceservice.aspect;


import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Aspect
@Component
public class BalanceServiceAspect {

    private final Logger logger = LoggerFactory.getLogger(BalanceServiceAspect.class);

    private long countCallMethodChangeBalance = 0L; //TODO нужна ли тут многопоточка?

    private long countCallMethodGetBalance = 0L;

    private long sumCallMethod;

    {
        startTimer();
    }

    @AfterReturning("execution(public void com.example.balanceservice.service.impl.BalanceServiceImpl.changeBalance(..))")
    public void afterReturningChangeBalance() {
        countCallMethodChangeBalance++;
        countCallMethodInTime();
    }


    @AfterReturning("execution(* com.example.balanceservice.service.impl.BalanceServiceImpl.getBalance(*))")
    public void afterReturningGetBalance() {
        countCallMethodGetBalance++;
        countCallMethodInTime();
    }


    private void countCallMethodInTime() {
        logger.info("\n" + "afterReturningGetBalance = "
                + countCallMethodGetBalance + " call \n" +
                "countCallMethodChangeBalance = "
                + countCallMethodChangeBalance + " call \n" + "sum = " +
                sumCallMethod + " call in 1 sec");
    }

    private void startTimer() {
        Timer myTimer = new Timer();

        myTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sumCallMethod = countCallMethodChangeBalance + countCallMethodGetBalance;
                countCallMethodChangeBalance = 0;
                countCallMethodGetBalance = 0;
            }
        }, 0, 1000); // каждые 1 секунд
    }
}
