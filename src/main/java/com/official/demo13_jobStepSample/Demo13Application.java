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
 * 本demo注意点：每次执行前，都先执行：init-database-sql/schema-truncate-oracle10g.sql
 * 因为会存在job不能重复启动的问题，好像不是JOB ID重复的问题，目前原因不明
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