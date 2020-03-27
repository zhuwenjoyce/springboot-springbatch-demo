package com.official.demo10_hibernateJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * SpringBatch官网github地址：
 * https://github.com/spring-projects/spring-batch
 *
 * this demo 例子来自于：
 * spring-batch-samples/src/main/resources/jobs/hibernateJob.xml
 *  我修改了com.official.demo10_hibernateJob.domain.HibernateCreditDao.writeCredit(CustomerCredit customerCredit)方法，以便观察update结果
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 */
@SpringBootApplication
public class Demo10Application {
    private static Logger logger = LoggerFactory.getLogger(Demo10Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo10Application.class, args);
        logger.info("demo 10 SpringBoot 启动成功！");
    }

}