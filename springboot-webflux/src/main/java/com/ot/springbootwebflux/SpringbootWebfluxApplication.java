package com.ot.springbootwebflux;

import com.ot.springbootwebflux.entity.MyEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootApplication
public class SpringbootWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringbootWebfluxApplication.class);
        ConfigurableApplicationContext ac = springApplication.run(args);
    }


    /**
     * spring boot应用在启用之后，会遍历CommandLineRunner的实例并运行他们的run方法
     * <p>
     * 1. 对于复杂的Bean只能通过Java Config的方式配置，这也是为什么Spring3之后官方推荐这种配置方式的原因，这段代码可以
     * 放到配置类中，本例我们就直接放到启动类`WebFluxDemoApplication`了；
     * 2. `MongoOperations`提供对MongoDB的操作方法，由Spring注入的mongo实例已经配置好，直接使用即可；
     * 3. `CommandLineRunner`也是一个函数式接口，其实例可以用lambda表达；
     * 4. 如果有，先删除collection，生产环境慎用这种操作；
     * 5. 创建一个记录个数为10的capped的collection，容量满了之后，新增的记录会覆盖最旧的。
     * capped：覆盖，超出
     *
     * @param mongo
     * @return
     */
    @Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                mongo.dropCollection(MyEvent.class);
                //size指的是100000个字节
                mongo.createCollection(MyEvent.class, CollectionOptions.empty().maxDocuments(200).size(100000).capped());
            }
        };
    }

}
