package com.official.demo11_infiniteLoopJob.run;

import com.official.demo10_hibernateJob.domain.MyStartJob;
import com.official.demo11_infiniteLoopJob.domain.DummyItemWriter;
import com.official.demo11_infiniteLoopJob.domain.GeneratingTradeItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.factory.FaultTolerantStepFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
//@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class RunJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//    @Autowired
//    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;
    @Autowired
    @Qualifier("oracleDataSource")
    private DataSource dataSource;
    @Autowired
    @Qualifier("txManager")
    private DataSourceTransactionManager txManager;

    @Bean("jobBuilderFactory")
    public JobBuilderFactory getJobBuilderFactory(){
        JobBuilderFactory jobBuilder = new JobBuilderFactory(jobRepository);
        return jobBuilder;
    }

    /**
     * 注意！！！job一旦@Bean生成spring实例，就会自动运行！
     * @return
     */
    @Bean("infiniteLoopJob")
    public SimpleJob getSimpleJob(
            @Qualifier("jobBuilderFactory") JobBuilderFactory jobBuilderFactory
    ) throws Exception {

        /*
       这里的jobName设置为随机，是因为第一次job如果是意外结束状态为running，第二次启动job就会报错：
       org.springframework.batch.core.repository.JobExecutionAlreadyRunningException:
       A job execution for this job is already running: JobExecution: id=1
        即使重启机器也是一样，原因是：因为该job实例已经持久化存储到DB了，你查这个表就知道：select * from  BATCH_JOB_INSTANCE ;
        解决方法是：调用schema-truncate-oracle10g.sql清空所有的SpringBatch表
        */
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "infiniteLoopJob-" + uuid;

        JobBuilder jobBuilder = jobBuilderFactory.get(jobName);
        MyStartJob myStartJob = new MyStartJob();
        SimpleJobBuilder simpleJobBuilder = jobBuilder.start(myStartJob);

        GeneratingTradeItemReader itemReader = new GeneratingTradeItemReader();
        itemReader.setLimit(5);
        DummyItemWriter itemWriter = new DummyItemWriter();

        FaultTolerantStepFactoryBean infiniteStep = new FaultTolerantStepFactoryBean();
        infiniteStep.setItemReader(itemReader);
        infiniteStep.setItemWriter(itemWriter);
        infiniteStep.setBeanName("infiniteStepName"); // A Step must have a name
        infiniteStep.setJobRepository(jobRepository); // JobRepository is mandatory
        infiniteStep.setTransactionManager(txManager); // A transaction manager must be provided
        Step step = infiniteStep.getObject();

        List<Step> steps = new ArrayList<>();
        steps.add(step);

        RunIdIncrementer incrementer = new RunIdIncrementer();

        SimpleJob infiniteLoopJob = (SimpleJob) simpleJobBuilder.build();
        infiniteLoopJob.setJobParametersIncrementer(new RunIdIncrementer());
        infiniteLoopJob.setJobRepository(jobRepository);
        infiniteLoopJob.setName(jobName);
        infiniteLoopJob.setRestartable(true);
        infiniteLoopJob.setJobParametersIncrementer(incrementer);
        infiniteLoopJob.setSteps(steps);
        // No DataSource specified
        return infiniteLoopJob;
    }
}
