package com.official.demo14_loopFlowSample.run;

import com.official.demo14_loopFlowSample.domain.GeneratingTradeItemReader;
import com.official.demo14_loopFlowSample.domain.GeneratingTradeResettingListener;
import com.official.demo14_loopFlowSample.domain.ItemTrackingTradeItemWriter;
import com.official.demo14_loopFlowSample.domain.MyStep2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep1AndStep2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("itemWriter")
    public ItemTrackingTradeItemWriter getItemTrackingTradeItemWriter(
    ){
        ItemTrackingTradeItemWriter writer = new ItemTrackingTradeItemWriter();
        return writer;
    }

    @Bean("itemGenerator")
    public GeneratingTradeItemReader getGeneratingTradeItemReader(){
        GeneratingTradeItemReader reader = new GeneratingTradeItemReader();
        reader.setLimit(1);
        return reader;
    }

    @Bean("resettingListener")
    public GeneratingTradeResettingListener getGeneratingTradeResettingListener(
            @Qualifier("itemGenerator") GeneratingTradeItemReader itemGenerator
    ){
        GeneratingTradeResettingListener listener = new GeneratingTradeResettingListener();
        listener.setReader(itemGenerator);
        return listener;
    }

    @Bean("step1")
    public TaskletStep buildStep1(
            @Qualifier("resettingListener") GeneratingTradeResettingListener resettingListener
            ,@Qualifier("itemGenerator") GeneratingTradeItemReader itemReader
            ,@Qualifier("itemWriter") ItemTrackingTradeItemWriter itemWriter
    ){
        SimpleStepBuilder simpleStepBuilder = this.stepBuilderFactory.get("step1")
                .<String, String>chunk(1);
        simpleStepBuilder = simpleStepBuilder.reader(itemReader).writer(itemWriter);
        simpleStepBuilder.listener(resettingListener);  // 监听器会在step执行完毕之后执行 afterStep
        TaskletStep taskletStep = simpleStepBuilder.build();
        taskletStep.setName("step1Nmae");
        return taskletStep;
    }

    @Bean("step2")
    public Step buildStep2(){
        MyStep2 myStep2 = new MyStep2();
        return myStep2;
    }
}
