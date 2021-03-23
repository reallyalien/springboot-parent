package com.ot.schedule.task;

import java.util.Timer;
import java.util.TimerTask;

public class JdkTimerTask {

    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("当前线程：" + Thread.currentThread());
            }
        };
        long delay = 3 * 1000;
        long period = 1000;
        System.out.println("主线程：" + Thread.currentThread());
        //第二个参数表示的意思
        /*
            重复任务的周期（毫秒）。
            正值表示固定速率执行。
            负值表示固定延迟执行。
            值0表示非重复任务。
         */
        timer.schedule(task, delay, period);
    }
}
