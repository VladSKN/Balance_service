package com.example.balanceservice.aspect;


import com.example.balanceservice.statistics.RateStatisticsLoggerJob;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BalanceServiceAspect {

    private final RateStatisticsLoggerJob loggerJob;


    public BalanceServiceAspect(RateStatisticsLoggerJob loggerJob) {
        this.loggerJob = loggerJob;
    }


    @AfterReturning(
            "execution(public void com.example.balanceservice.service.impl.BalanceServiceImpl.changeBalance(..))")
    public void afterReturningChangeBalance() {
        loggerJob.getLastSecondStatisticsChangeBalance();
    }


    @AfterReturning("execution(* com.example.balanceservice.service.impl.BalanceServiceImpl.getBalance(*))")
    public void afterReturningGetBalance() {
        loggerJob.getLastSecondStatisticsGetBalance();
    }
}
