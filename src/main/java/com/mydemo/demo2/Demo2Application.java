package com.mydemo.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileNotFoundException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.howtodoinjava.global", "com.mydemo.demo2"})
@EnableScheduling //使StartupJob的@Scheduled注解生效
public class Demo2Application {

    private static Logger logger = LoggerFactory.getLogger(Demo2Application.class);

    public static void main(String[] args) throws FileNotFoundException {
        ConfigurableApplicationContext applicationContext =  SpringApplication.run(Demo2Application.class, args);
        logger.info("demo 2 SpringBoot 启动成功！");
    }
}
