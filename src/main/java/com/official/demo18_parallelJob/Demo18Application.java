package com.official.demo18_parallelJob;

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
 * spring-batch-samples/src/main/resources/jobs/parallelJob.xml
 *
 * com.official.demo18_parallelJob.run.BuildStep_loading:
 *          //为每个任务触发一个新线程的实现，异步地执行它。
 *         //支持通过“concurrencyLimit”bean属性限制并发线程。默认情况下，并发线程的数量是无限的。
 *         //注意:此实现不重用线程!考虑一个线程池TaskExecutor实现，特别是为执行大量的短期任务。
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo18_parallelJob"})
public class Demo18Application {
    private static Logger logger = LoggerFactory.getLogger(Demo18Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo18Application.class, args);
        logger.info("demo 18 SpringBoot 启动成功！");
    }

}