package com.official.demo11_infiniteLoopJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * SpringBatch官网github地址：
 * https://github.com/spring-projects/spring-batch
 *
 * this demo 例子来自于：
 * spring-batch-samples/src/main/resources/jobs/infiniteLoopJob.xml
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo11_infiniteLoopJob"})
public class Demo11Application {
    private static Logger logger = LoggerFactory.getLogger(Demo11Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo11Application.class, args);
        logger.info("demo 11 SpringBoot 启动成功！");
    }

}