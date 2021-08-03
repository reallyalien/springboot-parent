package com.ot.es.lucene.test;

import org.junit.Test;

import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

public class Queue {

    @Test
    public void test1() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        System.out.println(queue);

    }
}
