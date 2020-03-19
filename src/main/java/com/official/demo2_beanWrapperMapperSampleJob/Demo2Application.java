package com.official.demo2_beanWrapperMapperSampleJob;

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
 * demo2 例子来自于：
 * spring-batch-samples/src/main/resources/jobs/beanWrapperMapperSampleJob.xml
 *  我稍微改良了下
 *
 * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.official.demo2_beanWrapperMapperSampleJob"})
public class Demo2Application {
    private static Logger logger = LoggerFactory.getLogger(Demo2Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo2Application.class, args);
        logger.info("demo 2 SpringBoot 启动成功！");
    }
    /*
    console will print:

    INFO  c.h.example1.run.ConfigJob - job[beanWrapperMapperJob-9b06c769c5794503afcf629b031caf1d]开始执行了。
    INFO  o.s.b.c.l.support.SimpleJobLauncher - Job: [SimpleJob: [name=beanWrapperMapperJob-9b06c769c5794503afcf629b031caf1d]] launched with the following parameters: [{run.id=1}]
    INFO  o.s.batch.core.job.SimpleStepHandler - Executing step: [taskletStepName1]
    INFO  o.s.batch.core.step.AbstractStep - Step: [taskletStepName1] executed in 49ms
    INFO  o.s.batch.core.job.SimpleStepHandler - Executing step: [taskletStepName2]
    INFO  o.s.batch.core.step.AbstractStep - Step: [taskletStepName2] executed in 14ms
    INFO  o.s.b.c.l.support.SimpleJobLauncher - Job: [SimpleJob: [name=beanWrapperMapperJob-9b06c769c5794503afcf629b031caf1d]] completed with the following parameters: [{run.id=1}] and the following status: [COMPLETED] in 84ms
    INFO  c.o.d.Demo2Application - demo 2 SpringBoot 启动成功！
     */

}
