package com.official.demo12_ioSampleJob;

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
 * spring-batch-samples/src/main/resources/jobs/ioSampleJob.xml
 *
 * Note:  这个 ioSampleJob.xml 例子没什么好看的，纯粹是介绍如何在processor中处理东西，略过。。。。
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo12_ioSampleJob"})
public class Demo12Application {
    private static Logger logger = LoggerFactory.getLogger(Demo12Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo12Application.class, args);
        logger.info("demo 11 SpringBoot 启动成功！");
    }

}