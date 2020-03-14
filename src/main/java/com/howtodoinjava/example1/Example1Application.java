package com.howtodoinjava.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 例子来源参考： https://howtodoinjava.com/spring-batch/java-config-multiple-steps/   Spring Batch – Java Config
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.howtodoinjava.global", "com.howtodoinjava.example1"})
public class Example1Application implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(Example1Application.class);
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Example1Application.class, args);
        logger.info("example 1 SpringBoot 启动成功！");
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
