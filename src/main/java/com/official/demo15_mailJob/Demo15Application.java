package com.official.demo15_mailJob;

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
 * spring-batch-samples/src/main/resources/jobs/mailJob.xml
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo15_mailJob"})
public class Demo15Application {
    private static Logger logger = LoggerFactory.getLogger(Demo15Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo15Application.class, args);
        logger.info("demo 15 SpringBoot 启动成功！");
    }

}