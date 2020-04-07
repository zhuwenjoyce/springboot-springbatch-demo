package com.mybatch.mybatch4_loop_read.run;

import com.mybatch.mybatch4_loop_read.domain.Batch4ItemReader;
import com.mybatch.mybatch4_loop_read.domain.Batch4ItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class RunJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    public TaskletStep getTaskletStep(){
        StepBuilder stepBuilder = stepBuilderFactory.get("Batch4Step");
        SimpleStepBuilder builder = stepBuilder.chunk(3); // reader一次处理3行
        builder.reader(new Batch4ItemReader()).writer(new Batch4ItemWriter());
        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

    @PostConstruct
    public void runJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        // 只有每次参数不一样，job才可重复执行
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        SimpleJob job = (SimpleJob) this.jobBuilderFactory.get("loopReaderJob")
                .start(getTaskletStep()).build(); // 流程逻辑设置结束
        job.setRestartable(true);
        jobLauncher.run(job,jobParameters);
    }
}
