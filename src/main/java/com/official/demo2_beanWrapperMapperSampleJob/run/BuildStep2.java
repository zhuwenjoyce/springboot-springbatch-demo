package com.official.demo2_beanWrapperMapperSampleJob.run;

import com.howtodoinjava.example1.run.ConfigJob;
import com.official.demo2_beanWrapperMapperSampleJob.domain.Person;
import com.official.demo2_beanWrapperMapperSampleJob.job.PersonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep2 {

    private static Logger logger = LoggerFactory.getLogger(ConfigJob.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("person")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)  //  ==  scope="prototype"
    public Person getPerson(){
        return new Person();
    }

    @Bean("personFieldSetMapper")
    public BeanWrapperFieldSetMapper getBeanWrapperFieldSetMapper(){
        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper();
        mapper.setPrototypeBeanName("person");
        return mapper;
    }

    @Bean("personTokenizer")
    public FixedLengthTokenizer getFixedLengthTokenizer(){
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("Title", "FirstName", "LastName", "Age", "Address.AddrLine1", "children[0].name", "children[1].name");
        tokenizer.setColumns(
                new Range(1,5),
                new Range(6,20),
                new Range(21,40),
                new Range(41,45),
                new Range(46,55),
                new Range(56,65),
                new Range(66,75)
        );
        return tokenizer;
    }

    @Bean("personFileItemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("personTokenizer") FixedLengthTokenizer personTokenizer,
            @Qualifier("personFieldSetMapper") BeanWrapperFieldSetMapper personFieldSetMapper
    ){
        FlatFileItemReader reader = new FlatFileItemReader();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource inputFile = resourceLoader.getResource("data/demo2/20070122.teststream.ImportPersonDataStep.txt");
        reader.setResource(inputFile);
        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(personTokenizer);
        lineMapper.setFieldSetMapper(personFieldSetMapper);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean("personWriter")
    public PersonWriter getPersonWriter(){
        PersonWriter writer = new PersonWriter();
        return writer;
    }

    @Bean("step2")
    public Step getStep2(@Qualifier("personFileItemReader") FlatFileItemReader personFileItemReader, @Qualifier("personWriter") PersonWriter personWriter) {
        TaskletStep step = this.stepBuilderFactory.get("step")
                .chunk(1) // 这里的chunkSize 就是 commit-interval <chunk reader="reader" writer="writer" commit-interval="5"/>
                .reader(personFileItemReader)
                .processor(new PassThroughItemProcessor())
                .writer(personWriter)
                .build();
        step.setName("taskletStepName2");
        return step;
    }
}
