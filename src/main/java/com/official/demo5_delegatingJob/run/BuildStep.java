package com.official.demo5_delegatingJob.run;

import com.official.demo5_delegatingJob.domain.PersonService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.PropertyExtractingDelegatingItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("delegateObject")
    public PersonService getPersonService(){
        PersonService personService = new PersonService();
        return personService;
    }

    @Bean("reader")
    public ItemReaderAdapter getItemReaderAdapter(
            @Qualifier("delegateObject") PersonService personService
    ){
        ItemReaderAdapter adapter = new ItemReaderAdapter();
        adapter.setTargetObject(personService);
        adapter.setTargetMethod("getData");
        return adapter;
    }

    @Bean("writer")
    public PropertyExtractingDelegatingItemWriter getPropertyExtractingDelegatingItemWriter(
            @Qualifier("delegateObject") PersonService personService
    ){
        PropertyExtractingDelegatingItemWriter writer = new PropertyExtractingDelegatingItemWriter();
        writer.setTargetObject(personService);
        writer.setTargetMethod("processPerson");
        writer.setFieldsUsedAsTargetMethodArguments(new String[]{"firstName","address.city"});
        return writer;
    }

    @Bean("step1")
    public Step getStep1(
            @Qualifier("reader") ItemReaderAdapter reader
            , @Qualifier("writer") PropertyExtractingDelegatingItemWriter writer
    ) {

        StepBuilder stepBuilder = stepBuilderFactory.get("step");

        SimpleStepBuilder builder = stepBuilder.chunk(1);
        builder.reader(reader);
        builder.writer(writer);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

}
