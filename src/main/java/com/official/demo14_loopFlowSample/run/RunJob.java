package com.official.demo14_loopFlowSample.run;

import com.official.demo14_loopFlowSample.domain.LimitDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Configuration
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
    @Qualifier("step1")
    TaskletStep step1;
    @Autowired
    @Qualifier("step2")
    Step step2;

    @Bean("limitDecider")
    public LimitDecider getLimitDecider(){
        LimitDecider decider = new LimitDecider();
        decider.setLimit(9);
        return decider;
    }

    @PostConstruct
    public void getJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        // 只有每次参数不一样，job才可重复执行
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        LimitDecider limitDecider = getLimitDecider();

        FlowJobBuilder flowJobBuilder = this.jobBuilderFactory.get("loopFlowSample")
                .start(step1) // 先执行step1
                .next(limitDecider) // limitDecider是决策者，决策者先看看step1的上下文都带来了什么，再决定返回什么状态
                .on("CONTINUE").to(step2) // 如果决策者返回状态：CONTINUE，就去执行step2，其他同理
                .on(FlowExecutionStatus.STOPPED.getName()).stop() // 如果决策者返回状态：STOPPED，job终止
                .end(); // 流程逻辑设置结束

        FlowJob flowJob = (FlowJob) flowJobBuilder.build();
        flowJob.setRestartable(true); // 设置为true, 如果上一次执行FAILED失败了，那么允许下一次再次启动执行job
        jobLauncher.run(flowJob,jobParameters);
    }
}
