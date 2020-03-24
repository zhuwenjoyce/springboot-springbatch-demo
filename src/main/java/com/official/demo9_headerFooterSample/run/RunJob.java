package com.official.demo9_headerFooterSample.run;

import com.howtodoinjava.example1.run.ConfigJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class RunJob {

    private static Logger logger = LoggerFactory.getLogger(ConfigJob.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    /**
     * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
     * @return
     */
    @Bean("footballJob")
    public Job job(
            @Qualifier("step1") Step step1
    ) {
        /*
       这里的jobName设置为随机，是因为第一次job如果是意外结束状态为running，第二次启动job就会报错：
       org.springframework.batch.core.repository.JobExecutionAlreadyRunningException:
       A job execution for this job is already running: JobExecution: id=1
        即使重启机器也是一样，原因是：因为该job实例已经持久化存储到DB了，你查这个表就知道：select * from  BATCH_JOB_INSTANCE ;
        解决方法是：调用schema-truncate-oracle10g.sql清空所有的SpringBatch表
        */
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "footballJob-" + uuid;

        JobBuilder jobBuilder = jobBuilderFactory.get(jobName);
        jobBuilder.incrementer(new RunIdIncrementer());

        SimpleJobBuilder simpleJobBuilder = jobBuilder.start(step1);

        SimpleJob simpleJob = (SimpleJob) simpleJobBuilder.build();
        simpleJob.setName(jobName);
        return simpleJob;
    }
}
