package com.ot.springboot.hadoop;

import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;

/**
 * 为了使用Hadoop，首先需要通过创建Configuration对象来配置它。 该配置保存有关job tracker，
 * 输入，输出格式和map reduce作业的其他各种参数的信息。
 */
public class Config extends SpringHadoopConfigurerAdapter {
    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        config.fileSystemUri("");
        super.configure(config);
    }
}
