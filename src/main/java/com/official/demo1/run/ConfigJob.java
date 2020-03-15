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

import java.util.UUID;

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

    /**
     * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
     * @param step1
     * @return
     */
    @Bean("myjob")
    public Job job(@Qualifier("step1") Step step1) {
        /*
       这里的jobName设置为随机，是因为第一次job如果是意外结束状态为running，第二次启动job就会报错：
       org.springframework.batch.core.repository.JobExecutionAlreadyRunningException:
       A job execution for this job is already running: JobExecution: id=1
        即使重启机器也是一样，原因是：因为该job实例已经持久化存储到DB了，你查这个表就知道：select * from  BATCH_JOB_INSTANCE ;
        解决方法是：调用schema-truncate-oracle10g.sql清空所有的SpringBatch表
        */
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "loopJob-" + uuid;
        return jobBuilderFactory.get(jobName)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
}
