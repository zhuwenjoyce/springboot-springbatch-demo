package com.official.demo1.run;

import com.official.demo1.job.InfiniteLoopReader;
import com.official.demo1.job.InfiniteLoopWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class ConfigJob {

    private static Logger logger = LoggerFactory.getLogger(ConfigJob.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("reader")
    public InfiniteLoopReader getInfiniteLoopReader(){
        return new InfiniteLoopReader();
    }

    @Bean("writer")
    public InfiniteLoopWriter getInfiniteLoopWriter(){
        return new InfiniteLoopWriter();
    }

    @Bean("step1")
    public Step step(@Qualifier("reader") InfiniteLoopReader reader,@Qualifier("writer") InfiniteLoopWriter writer) {
        return this.stepBuilderFactory.get("step")
                .chunk(1) // 这里的chunkSize 就是 commit-interval <chunk reader="reader" writer="writer" commit-interval="5"/>
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean("myjob")
    public Job job(@Qualifier("step1") Step step1) {
        int randomInt = new Random().nextInt(10000);
//       这里的jobName设置为随机，是因为第一次job如果是意外结束状态为running，第二次启动job就会报错：
//       org.springframework.batch.core.repository.JobExecutionAlreadyRunningException:
//       A job execution for this job is already running: JobExecution: id=1
        String jobName = "loopJob" + randomInt;
        return jobBuilderFactory.get(jobName)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
}
