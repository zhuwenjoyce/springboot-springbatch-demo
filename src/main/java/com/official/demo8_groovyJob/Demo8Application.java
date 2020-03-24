package com.official.demo8_groovyJob;

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
 * spring-batch-samples/src/main/resources/jobs/groovyJob.xml
 *  我稍微改良了下
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo8_groovyJob"})
public class Demo8Application {
    private static Logger logger = LoggerFactory.getLogger(Demo8Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo8Application.class, args);
        logger.info("demo 8 SpringBoot 启动成功！");
    }

}