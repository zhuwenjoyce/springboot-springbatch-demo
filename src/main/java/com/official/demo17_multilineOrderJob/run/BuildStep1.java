package com.official.demo17_multilineOrderJob.run;

import com.official.demo17_multilineOrderJob.domain.OrderItemReader;
import com.official.demo17_multilineOrderJob.domain.OrderLineAggregator;
import com.official.demo17_multilineOrderJob.domain.OrderValidator;
import com.official.demo17_multilineOrderJob.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.Map;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("validator")
    public SpringValidator getSpringValidator(){
        SpringValidator validator = new SpringValidator();
        validator.setValidator(new OrderValidator());
        return validator;
    }

    @Bean("processor")
    public ValidatingItemProcessor getValidatingItemProcessor(
            @Qualifier("validator") SpringValidator validator
    ){
        ValidatingItemProcessor processor = new ValidatingItemProcessor(validator);
        processor.setFilter(true);
        return processor;
    }

    @Bean("fileItemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("orderFileTokenizer") PatternMatchingCompositeLineTokenizer orderFileTokenizer
    ){
        String fileName = "data/demo17/multilineOrderInput.txt";
        ClassPathResource inputResource = new ClassPathResource(fileName,this.getClass().getClassLoader());
        if(!inputResource.exists() || !inputResource.isReadable()){
            logger.error(fileName+" file is not exists or is not readable.");
            System.exit(1);
        }

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(orderFileTokenizer);
        lineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());

        FlatFileItemReader fileItemReader = new FlatFileItemReader();
        fileItemReader.setResource(inputResource);
        fileItemReader.setLineMapper(lineMapper);
        return fileItemReader;
    }

    @Bean("reader")
    public OrderItemReader getOrderItemReader(
            @Qualifier("fileItemReader") FlatFileItemReader fileItemReader
    ){
        OrderItemReader reader = new OrderItemReader();
        reader.setFieldSetReader(fileItemReader);
        reader.setHeaderMapper(new HeaderFieldSetMapper());
        reader.setCustomerMapper(new CustomerFieldSetMapper());
        reader.setAddressMapper(new AddressFieldSetMapper());
        reader.setBillingMapper(new BillingFieldSetMapper());
        reader.setItemMapper(new OrderItemFieldSetMapper());
        reader.setShippingMapper(new ShippingFieldSetMapper());
        return reader;
    }

    @Value("${file.output.directory}")
    private String fileOutputDirectory;

    @Bean("fileItemWriter")
    public FlatFileItemWriter getFlatFileItemWriter(
            @Qualifier("outputAggregators") Map outputAggregators
    ){
        File outputDirectory = new File(fileOutputDirectory + "demo17_output/");
        if(!outputDirectory.exists()){
            Boolean isCreate =  outputDirectory.mkdirs();
            logger.info("创建新文件夹成功：" + outputDirectory.getAbsolutePath());
        }

        String outputFile = outputDirectory + "/demo17_output.txt";

        OrderLineAggregator lineAggregator = new OrderLineAggregator();
        lineAggregator.setAggregators(outputAggregators);

        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setResource(new FileSystemResource(outputFile));
        writer.setLineAggregator(lineAggregator);

        return writer;
    }

    @Bean("step1")
    public TaskletStep getStep1(
            @Qualifier("reader") OrderItemReader reader
            ,@Qualifier("fileItemReader") FlatFileItemReader fileItemReader
            ,@Qualifier("fileItemWriter") FlatFileItemWriter fileItemWriter
            ,@Qualifier("processor") ValidatingItemProcessor processor
    ){
        SimpleStepBuilder simpleStepBuilder = this.stepBuilderFactory.get("step1")
                .<String, String>chunk(5);  // commit-interval="5"
        TaskletStep taskletStep = simpleStepBuilder
                .reader(reader)
                .processor(processor)
                .writer(fileItemWriter)
                .stream(fileItemReader)
                .stream(fileItemWriter)
                .build();
        return taskletStep;
    }
}
