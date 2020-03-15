package com.howtodoinjava.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 例子来源参考： https://howtodoinjava.com/spring-batch/java-config-multiple-steps/   Spring Batch – Java Config
 * 我去掉了例子中的启动类实现了CommandLineRunner这个类，因为 job一旦@Bean生成spring实例，就会自动运行
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.howtodoinjava.example1"})
public class Example1Application {
    private static Logger logger = LoggerFactory.getLogger(Example1Application.class);
    @Autowired
    JobLauncher jobLauncher;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Example1Application.class, args);
        logger.info("example 1 SpringBoot 启动成功！");
    }
}
