package com.official.demo14_loopFlowSample;

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
 * spring-batch-samples/src/main/resources/jobs/loopFlowSample.xml
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo14_loopFlowSample"})
public class Demo14Application {
    private static Logger logger = LoggerFactory.getLogger(Demo14Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo14Application.class, args);
        logger.info("demo 14 SpringBoot 启动成功！");
    }

}