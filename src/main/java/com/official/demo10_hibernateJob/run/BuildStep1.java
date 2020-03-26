package com.official.demo10_hibernateJob.run;

import com.official.demo10_hibernateJob.domain.CustomerCreditIncreaseProcessor;
import com.official.demo10_hibernateJob.domain.HibernateAwareCustomerCreditItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep1 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("step1")
    public Step getPlayerloadStep(
            @Qualifier("hibernateItemReader") HibernateCursorItemReader hibernateItemReader
            ,@Qualifier("creditIncreaseProcessor") CustomerCreditIncreaseProcessor creditIncreaseProcessor
            ,@Qualifier("hibernateCreditWriter") HibernateAwareCustomerCreditItemWriter hibernateCreditWriter
    ){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "playerloadStepName-" + uuid;
        StepBuilder stepBuilder = stepBuilderFactory.get(jobName);


        SimpleStepBuilder builder = stepBuilder.chunk(3);
        builder.reader(hibernateItemReader);
        builder.processor(creditIncreaseProcessor);
        builder.writer(hibernateCreditWriter);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

}
