package com.ot.springboot.rabbit.topic;

import java.awt.*;

public class Window extends Frame {

    public static void main(String[] args) {

        Window fr = new Window("First Contianer!");//构造方法
        fr.setSize(500, 300);                //设置Frame的大小，默认为（0,0）
        fr.setBackground(Color.white);    //设置Frame的背景颜色为黄色，默认值为白色
        fr.setVisible(true);                //设置Frame为可见，默认为不可见

    }

    public Window(String str) {
        super(str);    //调用父类构造方法
    }

}
