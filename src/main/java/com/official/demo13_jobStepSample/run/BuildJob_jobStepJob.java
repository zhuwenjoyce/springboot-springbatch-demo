package com.official.demo13_jobStepSample.run;

import com.official.demo13_jobStepSample.domain.MyStartJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildJob_jobStepJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;


    @Bean("jobStepJob")
    public Job getTradeJob(
            @Qualifier("tradeJob") Job tradeJob
    ){
        JobStep jobStep = new JobStep();
        jobStep.setJob(tradeJob);
        jobStep.setName("jobStepJob.step1");

        JobBuilder jobBuilder = jobBuilderFactory.get("jobStepJob");
        MyStartJob myStartJob = new MyStartJob();
        SimpleJobBuilder simpleJobBuilder = jobBuilder.start(myStartJob);
        SimpleJob simpleJob = (SimpleJob) simpleJobBuilder.build();
        simpleJob.setRestartable(true); // 可重复执行job

        simpleJob.addStep(jobStep);

        return simpleJob;
    }
}
