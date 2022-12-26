package com.example.balanceservice.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RateStatisticsLoggerJob {

    private static final Logger logger = LoggerFactory.getLogger(RateStatisticsLoggerJob.class);

    private final AtomicLong countCallMethodChangeBalance = new AtomicLong();

    private final AtomicLong countCallMethodGetBalance = new AtomicLong();

    private final AtomicLong sumCallMethod = new AtomicLong();


    public RateStatisticsLoggerJob() {
        startTimer();
    }

    public void getLastSecondStatisticsGetBalance() {
        countCallMethodGetBalance.getAndIncrement();
        countCallMethodInTime();
    }

    public void getLastSecondStatisticsChangeBalance() {
        countCallMethodChangeBalance.getAndIncrement();
        countCallMethodInTime();
    }

    private void startTimer() {
        Timer myTimer = new Timer();

        // time in milliseconds
        long periodStatistics = 1000;
        myTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sumCallMethod.set(countCallMethodChangeBalance.get() + countCallMethodGetBalance.get());
                countCallMethodChangeBalance.set(0L);
                countCallMethodGetBalance.set(0L);
            }
        }, 0, periodStatistics);
    }

    private void countCallMethodInTime() {
        logger.info(
                "\nafterReturningGetBalance = {} call \ncountCallMethodChangeBalance = {} call \nsum = {} call in 1 sec",
                countCallMethodGetBalance, countCallMethodChangeBalance, sumCallMethod);
    }
}
