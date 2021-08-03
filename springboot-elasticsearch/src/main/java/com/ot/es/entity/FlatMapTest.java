package com.ot.es.entity;

import java.util.Arrays;
import java.util.List;

public class FlatMapTest {

    public static void main(String[] args) {
        List<String> fun1 = Arrays.asList("one", "two", "three");
        List<String> fun2 = Arrays.asList("four", "five", "six");

        List<List<String>> nestedList = Arrays.asList(fun1, fun2);

        nestedList.stream().flatMap(list -> list.stream().map(String::toUpperCase)).forEach(System.out::println);
    }
}
