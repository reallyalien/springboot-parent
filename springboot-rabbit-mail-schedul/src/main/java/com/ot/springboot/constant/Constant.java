package com.ot.springboot.constant;

public class Constant {
    public static final String EXCHANGE = "exchange_mail_test";
    public static final String QUEUE = "queue_mail_test";
    public static final String ROUTE_KEY = "route_mail";

    private static final int SENDING=0;//投递中
    private static final int SENDER=1;//投递成功
    private static final int FAIL=2;//投递失败
    private static final int CONSUMERED=3;//已消费
}
