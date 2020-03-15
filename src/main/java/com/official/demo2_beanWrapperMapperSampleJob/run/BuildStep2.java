package com.official.demo2_beanWrapperMapperSampleJob.run;

import com.howtodoinjava.example1.run.ConfigJob;
import com.official.demo1_adhocLoopJob.job.InfiniteLoopReader;
import com.official.demo1_adhocLoopJob.job.InfiniteLoopWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep2 {

    private static Logger logger = LoggerFactory.getLogger(ConfigJob.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("step2")
    public Step getStep2(@Qualifier("reader") InfiniteLoopReader reader, @Qualifier("writer") InfiniteLoopWriter writer) {
        return this.stepBuilderFactory.get("step")
                .chunk(1) // 这里的chunkSize 就是 commit-interval <chunk reader="reader" writer="writer" commit-interval="5"/>
                .reader(reader)
                .writer(writer)
                .build();
    }
}
