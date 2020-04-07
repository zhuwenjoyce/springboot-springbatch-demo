package com.mybatch.mybatch3_partition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * need Oracle DB.
 * 此例子来自于：https://blog.csdn.net/TreeShu321/article/details/100861937
 */
@SpringBootApplication
public class Mybatch3Application {

    private static Logger logger = LoggerFactory.getLogger(Mybatch3Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =  SpringApplication.run(Mybatch3Application.class, args);
        logger.info("mybatch 3 SpringBoot 启动成功！");
    }
}
