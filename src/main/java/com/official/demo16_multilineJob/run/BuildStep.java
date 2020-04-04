package com.official.demo16_multilineJob.run;

import com.official.demo16_multilineJob.domain.AggregateItemFieldSetMapper;
import com.official.demo16_multilineJob.domain.AggregateItemReader;
import com.official.demo16_multilineJob.domain.TradeFieldSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Bean("fixedFileDescriptor")
    public PatternMatchingCompositeLineTokenizer getPatternMatchingCompositeLineTokenizer(){
        FixedLengthTokenizer beginRecordTokenizer = new FixedLengthTokenizer();
        beginRecordTokenizer.setColumns(new Range(1,5));

        FixedLengthTokenizer endRecordTokenizer = new FixedLengthTokenizer();
        endRecordTokenizer.setColumns(new Range(1,3));

        FixedLengthTokenizer tradeRecordTokenizer = new FixedLengthTokenizer();
        tradeRecordTokenizer.setNames("ISIN","Quantity","Price","Customer");
        tradeRecordTokenizer.setColumns(
                new Range(1,12),
                new Range(13,15),
                new Range(16,20),
                new Range(21,29)
        );

        Map<String, LineTokenizer> map = new HashMap<>();
        map.put("BEGIN*",beginRecordTokenizer); // 行头匹配分词器Tokenizer
        map.put("END*",endRecordTokenizer);  // 行尾匹配分词器Tokenizer
        map.put("*",tradeRecordTokenizer);  // 匹配成Trade对象

        PatternMatchingCompositeLineTokenizer tokenizer = new PatternMatchingCompositeLineTokenizer();
        tokenizer.setTokenizers(map);
        return tokenizer;
    }

    @Bean("fileItemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("fixedFileDescriptor") PatternMatchingCompositeLineTokenizer fixedFileDescriptor
    ) throws IOException {
        ClassPathResource inputResource = new ClassPathResource("data/demo16/multilineStep.txt",this.getClass().getClassLoader());

        logger.info(inputResource+"exists="+inputResource.exists() + ", isReadable=" + inputResource.isReadable());
        if(!inputResource.exists()){
            logger.error("["+inputResource.getFile().getAbsolutePath()+"]inpt file is not exists");
            System.exit(1);
        }

        AggregateItemFieldSetMapper fieldSetMapper = new AggregateItemFieldSetMapper();
        fieldSetMapper.setDelegate(new TradeFieldSetMapper());

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(fixedFileDescriptor);
        lineMapper.setFieldSetMapper(fieldSetMapper);


        FlatFileItemReader itemReader = new FlatFileItemReader();
        itemReader.setResource(inputResource);
        itemReader.setLineMapper(lineMapper);
        return itemReader;
    }

    @Value("${file.output.directory}")
    private String fileOutputDirectory;

    @Bean("writer")
    public FlatFileItemWriter getFlatFileItemWriter(){
        File outputDirectory = new File(fileOutputDirectory + "demo16_output/");
        if(!outputDirectory.exists()){
            Boolean isCreate =  outputDirectory.mkdirs();
            logger.info("创建新文件夹成功：" + outputDirectory.getAbsolutePath());
        }

        String outputFile = outputDirectory + "/demo16_CustomerReport1.txt";

        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setResource(new FileSystemResource(outputFile));
        writer.setLineAggregator(new PassThroughLineAggregator());
        return writer;
    }

    @Bean("reader")
    public AggregateItemReader getAggregateItemReader(
            @Qualifier("fileItemReader") FlatFileItemReader fileItemReader
    ){
        AggregateItemReader reader = new AggregateItemReader();
        reader.setItemReader(fileItemReader);
        return reader;
    }

    @Bean("step1")
    public TaskletStep getStep1(
            @Qualifier("reader") AggregateItemReader reader
            ,@Qualifier("writer") FlatFileItemWriter writer
            ,@Qualifier("fileItemReader") FlatFileItemReader fileItemReader
    ){
        TaskletStep taskletStep = this.stepBuilderFactory.get("step1")
                .<String, String>chunk(1)
                .reader(reader)
                .writer(writer)
                .stream(fileItemReader)
                .build();
        return taskletStep;
    }

}
