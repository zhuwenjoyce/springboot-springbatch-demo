package com.official.demo9_headerFooterSample;

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
 * spring-batch-samples/src/main/resources/jobs/headerFooterSample.xml
 *  我稍微改良了下
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 *
 * 知识重点：
 * 1、SimpleStepBuilder builder = stepBuilder.chunk(3); // reader一次处理3行
 * 2、writer.setHeaderCallback(headerCopier);    // 处理第一行
 *    writer.setFooterCallback(footerCallback);  // 处理最后一行
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo9_headerFooterSample"})
public class Demo9Application {
    private static Logger logger = LoggerFactory.getLogger(Demo9Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo9Application.class, args);
        logger.info("demo 9 SpringBoot 启动成功！");
    }

}