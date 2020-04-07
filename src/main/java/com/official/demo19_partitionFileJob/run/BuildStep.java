package com.official.demo19_partitionFileJob.run;

import com.official.demo19_partitionFileJob.domain.CustomerCreditIncreaseProcessor;
import com.official.demo19_partitionFileJob.domain.OutputFileListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.jsr.step.PartitionStep;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    public FlatFileItemReader getFlatFileItemReader(){
        FlatFileItemReader itemReader = new FlatFileItemReader();

        return itemReader;
    }

    public TaskletStep getTaskletStep(){

        CustomerCreditIncreaseProcessor processor = new CustomerCreditIncreaseProcessor();

        OutputFileListener fileNameListener = new OutputFileListener();
        fileNameListener.setPath("file:./build/output/file/");

        SimpleStepBuilder simpleStepBuilder = this.stepBuilderFactory.get("stagingStep")
                .<String, String>chunk(5);  // commit-interval="5"

        TaskletStep taskletStep = simpleStepBuilder
                .reader(getFlatFileItemReader())
                .processor(processor)
//                .writer(stagingItemWriter)
                .listener(fileNameListener)
                .build();
        return taskletStep;
    }

    @Bean("step1")
    public PartitionStep getPartitionStep(){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        partitioner.setResources(new Resource[]{
                resourceLoader.getResource("data/demo19/delimited.csv")
                ,resourceLoader.getResource("data/demo19/delimited2.csv")
        });


        PartitionStepBuilder partitionStepBuilder = stepBuilderFactory.get("step1")
                .partitioner("step1Name",partitioner)
                .gridSize(2)
                .taskExecutor(new SimpleAsyncTaskExecutor());

        PartitionStep partitionStep = (PartitionStep) partitionStepBuilder.build();
        partitionStepBuilder.step(getTaskletStep());

        return partitionStep;
    }
}
