package  com.ot.activiti.config;

import java.util.concurrent.LinkedBlockingQueue;

public class A {

   public static LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(3);

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            try {
                blockingQueue.put(1);
                blockingQueue.put(2);
                blockingQueue.put(3);
                blockingQueue.put(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(3000);
        blockingQueue.take();
        blockingQueue.take();
        blockingQueue.take();
        blockingQueue.take();
        System.out.println(blockingQueue);

    }
}
