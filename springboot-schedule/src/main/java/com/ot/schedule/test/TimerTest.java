package com.ot.schedule.test;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {


    public static void main(String[] args) {
        Timer timer=new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行线程："+Thread.currentThread());
            }
        };
        timer.schedule(task,10000,2000);
    }
}
