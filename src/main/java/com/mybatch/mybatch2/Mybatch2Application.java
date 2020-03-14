package com.mybatch.mybatch2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.howtodoinjava.global", "com.mybatch.mybatch2"})
public class Mybatch2Application {

    private static Logger logger = LoggerFactory.getLogger(Mybatch2Application.class);

    public static void main(String[] args) throws FileNotFoundException {
        ConfigurableApplicationContext applicationContext =  SpringApplication.run(Mybatch2Application.class, args);
        logger.info("mybatch 2 SpringBoot 启动成功！");
    }
}
