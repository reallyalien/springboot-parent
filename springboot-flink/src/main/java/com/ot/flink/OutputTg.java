package com.ot.flink;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class OutputTg<T> {
    String a;
    T b;

    public OutputTg(String a) throws InstantiationException, IllegalAccessException {
        this.a = a;
        System.out.println(this);

        Type genericSuperclass = this.getClass().getGenericSuperclass();
        System.out.println(genericSuperclass);

        ParameterizedType baseClassChild = (ParameterizedType) genericSuperclass;
        System.out.println(baseClassChild);

        Type actualTypeArgument = baseClassChild.getActualTypeArguments()[0];
        System.out.println(actualTypeArgument);

        Class<? extends Type> bClass = (Class<? extends Type>) actualTypeArgument;
        System.out.println(bClass);

        this.b = (T) bClass.newInstance();

    }


    public static void main(String[] args) {
        OutputTg<String> stringOutputTg = null;
        try {
            // error
            stringOutputTg = new OutputTg<String>("a");

            // 正常
//            stringOutputTg = new OutputTg<String>("a") {
//            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(stringOutputTg.a);
        System.out.println(stringOutputTg.b);
    }
}
