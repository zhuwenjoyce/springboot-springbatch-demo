package com.official.demo3_compositeItemWriterSampleJob.run;

import com.howtodoinjava.example1.run.ConfigJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep1 {

    private static Logger logger = LoggerFactory.getLogger(ConfigJob.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;


    @Bean("step1")
    public Step getStep1(@Qualifier("fileItemReader") FlatFileItemReader fileItemReader
            ,@Qualifier("processor") ValidatingItemProcessor processor
            ,@Qualifier("compositeWriter") CompositeItemWriter compositeWriter) {
        TaskletStep step = stepBuilderFactory.get("step1")
                .chunk(1) // 这里的chunkSize 就是 commit-interval <chunk reader="reader" writer="writer" commit-interval="5"/>
                .reader(fileItemReader)
                .processor(processor)
                .writer(compositeWriter)
                .build();
        step.setName("taskletStepName1");
        return step;
    }
}
