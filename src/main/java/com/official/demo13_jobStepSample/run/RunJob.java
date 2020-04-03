package com.official.demo13_jobStepSample.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class RunJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("step1")
    Step step1;

    @PostConstruct
    public void runJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = "tradeJob";

        SimpleJob tradeJob = (SimpleJob) jobBuilderFactory.get(jobName).start(step1).build();
        tradeJob.setRestartable(true); // 可重复执行job

        JobStep jobStep = new JobStep();
        jobStep.setJob(tradeJob);
        jobStep.setName("jobStepJob.step1");
        jobStep.setJobRepository(jobRepository);
        jobStep.setJobLauncher(jobLauncher);

        SimpleJob jobStepJob = (SimpleJob) jobBuilderFactory.get("jobStepJob").start(jobStep).build();
        jobStepJob.setRestartable(true); // 可重复执行job

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(jobStepJob,jobParameters);
    }

}
