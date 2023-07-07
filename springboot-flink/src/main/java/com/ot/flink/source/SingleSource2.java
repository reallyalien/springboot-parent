package com.ot.flink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;


//什么是并行度？
//​ 一个Flink程序由多个任务（Source、Transformation和Sink）组成。一个任务由多个并行实例（线程）来执行，一个任务的并行实例（线程）数目被称为该任务的并行度。
//并行度为1=无并行度
public class SingleSource2 implements SourceFunction<String> {
    //实现数据获取逻辑，并可以通过传入的参数ctx进行向下游的数据转发

    private volatile boolean complete = false;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        //SourceContext source函数用于发出元素和可能的watermark的接口，返回source生成的元素的类型。
        while (!complete) {
            String s = new Random().nextInt(9) + "," + String.valueOf(new Random().nextInt(50));
            ctx.collect(s);
            System.out.println(s);
            Thread.sleep(1000);
        }
    }

    //用来取消数据源，一般在run方法当中，会存在一个循环持续产生数据，cancel可以将run方法终止
    @Override
    public void cancel() {
        this.complete = true;
    }
}
