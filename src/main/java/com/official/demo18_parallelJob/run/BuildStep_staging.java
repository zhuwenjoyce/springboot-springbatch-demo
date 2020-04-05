package com.official.demo18_parallelJob.run;

import com.official.demo18_parallelJob.domain.StagingItemWriter;
import com.official.demo18_parallelJob.domain.TradeFieldSetMapper;
import com.official.demo18_parallelJob.domain.TradeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep_staging {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("fileItemReader")
    public FlatFileItemReader getFlatFileItemReader(){

        String fileName = "data/demo18/ImportTradeDataStep.txt";
        ClassPathResource inputResource = new ClassPathResource(fileName, this.getClass().getClassLoader());
        if(!inputResource.exists() || !inputResource.isReadable()){
            logger.error(fileName+" file is not exists or is not readable.");
            System.exit(1);
        }

        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("ISIN", "Quantity", "Price", "Customer");
        tokenizer.setColumns(
                new Range(1,12),
                new Range(13,15),
                new Range(16,20),
                new Range(21,29)
        );

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new TradeFieldSetMapper());

        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setLineMapper(lineMapper);
        reader.setResource(inputResource);
        return reader;
    }

    @Bean("stagingItemWriter")
    public StagingItemWriter getStagingItemWriter(
            @Qualifier("oracleDataSource") DataSource oracleDataSource
    ){
        OracleSequenceMaxValueIncrementer oracleIncrementer = new OracleSequenceMaxValueIncrementer();
        oracleIncrementer.setDataSource(oracleDataSource);
        oracleIncrementer.setIncrementerName("BATCH_STAGING_SEQ"); // TRADE_SEQ reference src/main/resources/init-sql/init-demo18.sql

        StagingItemWriter writer = new StagingItemWriter();
        writer.setDataSource(oracleDataSource);
        writer.setIncrementer(oracleIncrementer);
        return writer;
    }

    @Bean("stagingStep")
    public TaskletStep getStep1(
            @Qualifier("fileItemReader") FlatFileItemReader fileItemReader
            ,@Qualifier("stagingItemWriter") StagingItemWriter stagingItemWriter
    ){
        SpringValidator validator = new SpringValidator();
        validator.setValidator(new TradeValidator());
        ValidatingItemProcessor processor = new ValidatingItemProcessor(validator);

        SimpleStepBuilder simpleStepBuilder = this.stepBuilderFactory.get("stagingStep")
                .<String, String>chunk(2);  // commit-interval="2"
        TaskletStep taskletStep = simpleStepBuilder
                .reader(fileItemReader)
                .processor(processor)
                .writer(stagingItemWriter)
                .build();
        return taskletStep;
    }
}
