package com.ot.schedule.jdk;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        int i = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                i * 2,
                200,
                60,
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(128));
    }
}
