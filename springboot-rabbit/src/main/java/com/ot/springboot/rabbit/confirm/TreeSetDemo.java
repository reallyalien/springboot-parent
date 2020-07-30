package com.ot.springboot.rabbit.confirm;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class TreeSetDemo {

    public static void main(String[] args) {
        SortedSet<Integer> set = Collections.synchronizedSortedSet(new TreeSet<Integer>());
        set.add(2);
        set.add(1);
        set.add(4);
        set.add(10);
        System.out.println(set.headSet(4));
    }
}
