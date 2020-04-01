package com.official.demo13_jobStepSample;

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
 * spring-batch-samples/src/main/resources/jobs/jobStepSample.xml
 *
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo13_jobStepSample"})
public class Demo13Application {
    private static Logger logger = LoggerFactory.getLogger(Demo13Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo13Application.class, args);
        logger.info("demo 13 SpringBoot 启动成功！");
    }

}