//package com.ot.flink.sink;
//
//import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
//
//public class Sink extends RichSinkFunction<String> {
////
//    /**
//     * 将给定的值写入接收器，为每条记录调用函数
//     *
//     * @param value   获取到的值
//     * @param context 可用于获取有关输入记录的附加数据的上下文
//     * @throws Exception
//     */
//    @Override
//    public void invoke(String value, Context context) throws Exception {
//        System.out.println(value + "   " + context.getClass());
//    }
//
//
//    /**
//     * 此方法在数据处理结束时调
//     *
//     * @throws Exception
//     */
//    @Override
//    public void finish() throws Exception {
//        System.out.println("此方法在数据处理结束时调用");
//    }
//}
