package com.example.balanceservice.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class RateStatisticsLoggerJob {

    private static final Logger logger = LoggerFactory.getLogger(RateStatisticsLoggerJob.class);

    private long countCallMethodChangeBalance = 0L;

    private long countCallMethodGetBalance = 0L;

    private long sumCallMethod;


    public RateStatisticsLoggerJob() {
        startTimer();
    }

    public void getLastSecondStatisticsGetBalance() {
        countCallMethodGetBalance++;
        countCallMethodInTime();
    }

    public void getLastSecondStatisticsChangeBalance() {
        countCallMethodChangeBalance++;
        countCallMethodInTime();
    }

    private void startTimer() {
        Timer myTimer = new Timer();

        // time in milliseconds
        long periodStatistics = 1000;
        myTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sumCallMethod = countCallMethodChangeBalance + countCallMethodGetBalance;
                countCallMethodChangeBalance = 0;
                countCallMethodGetBalance = 0;
            }
        }, 0, periodStatistics);
    }

    private void countCallMethodInTime() {
        logger.info(
                "\nafterReturningGetBalance = {} call \ncountCallMethodChangeBalance = {} call \nsum = {} call in 1 sec",
                countCallMethodGetBalance, countCallMethodChangeBalance, sumCallMethod);
    }
}
