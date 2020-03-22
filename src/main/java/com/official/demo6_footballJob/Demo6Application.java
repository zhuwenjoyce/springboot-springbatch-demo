package com.official.demo6_footballJob;

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
 * spring-batch-samples/src/main/resources/jobs/footballJob.xml
 *  我稍微改良了下
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 *
 * 本demo注意点：每次执行前，都先执行：
 *   1、init-database-sql/schema-truncate-oracle10g.sql
 *   2、
 *   因为会存在job不能重复启动的问题，好像不是JOB ID重复的问题，目前原因不明
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo6_footballJob"})
public class Demo6Application {
    private static Logger logger = LoggerFactory.getLogger(Demo6Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo6Application.class, args);
        logger.info("demo 6 SpringBoot 启动成功！");
    }

}