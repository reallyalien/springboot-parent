package com.ot.springbootwebflux.demo;

import java.util.concurrent.TimeUnit;

public class TestCallBack {

    //回调函数
    public void test(CallBack callBack) throws InterruptedException {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callBack.run();
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {

        TestCallBack testCallBack = new TestCallBack();
        testCallBack.test(() -> {
            System.out.println("回调执行");
        });
        for (;;){
//            System.out.println("主线程执行");
        }
    }
}
