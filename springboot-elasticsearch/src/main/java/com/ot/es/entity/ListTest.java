package com.ot.es.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//foreach收集元素会出事

public class ListTest {
    public static void main(String[] args) {
        List<Integer> listOfIntegers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            listOfIntegers.add(i);
        }
        List<Integer> parallelStorage = new ArrayList<>() ;
        Stream<Integer> integerStream = listOfIntegers.parallelStream();
        List<Integer> collect = listOfIntegers
                .parallelStream()
                .filter(i -> i % 2 == 0)
                .collect(Collectors.toList());
        System.out.println();
        collect
                .stream()
                .forEachOrdered(e -> System.out.print(e + " "));
        System.out.println();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
