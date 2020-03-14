package com.official.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * com.official.demo1 例子来自于：
 * spring-batch-samples/src/main/resources/jobs/adhocLoopJob.xml
 *  我稍微改良了下
 *
 * SpringBatch官网github地址：
 * https://github.com/spring-projects/spring-batch
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.official.global", "com.official.demo1"})
public class Demo1Application {
    private static Logger logger = LoggerFactory.getLogger(Demo1Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo1Application.class, args);
        logger.info("demo 1 SpringBoot 启动成功！");
    }

}