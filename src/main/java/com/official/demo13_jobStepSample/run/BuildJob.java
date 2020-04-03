package com.official.demo13_jobStepSample.run;

import com.official.demo13_jobStepSample.domain.JdbcTradeDao;
import com.official.demo13_jobStepSample.domain.TradeFieldSetMapper;
import com.official.demo13_jobStepSample.domain.TradeValidator;
import com.official.demo13_jobStepSample.domain.TradeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
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
public class BuildJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("tradeDao")
    public JdbcTradeDao getJdbcTradeDao(
            @Qualifier("oracleDataSource") DataSource oracleDataSource
    ){
        OracleSequenceMaxValueIncrementer oracleIncrementer = new OracleSequenceMaxValueIncrementer();
        oracleIncrementer.setDataSource(oracleDataSource);
        oracleIncrementer.setIncrementerName("TRADE_SEQ"); // TRADE_SEQ reference src/main/resources/init-sql/init-demo13.sql

        JdbcTradeDao dao = new JdbcTradeDao();
        dao.setDataSource(oracleDataSource);
        dao.setIncrementer(oracleIncrementer);
        return dao;
    }

    @Bean("tradeWriter")
    public TradeWriter getTradeWriter(
            @Qualifier("tradeDao") JdbcTradeDao tradeDao
    ){
        TradeWriter writer = new TradeWriter();
        writer.setDao(tradeDao);
        return writer;
    }

    @Bean("fixedValidator")
    public SpringValidator getSpringValidator(){
        SpringValidator validator = new SpringValidator();
        validator.setValidator(new TradeValidator());
        return validator;
    }

    @Bean("processor")
    public ValidatingItemProcessor getValidatingItemProcessor(
            @Qualifier("fixedValidator") SpringValidator fixedValidator
    ){
        ValidatingItemProcessor processor = new ValidatingItemProcessor(fixedValidator);
        return processor;
    }

    @Bean("jobParameters")
    public JobParameters getJobParameters(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("input.file","data/demo13/TradeDataStep.txt")
                .toJobParameters();

        return jobParameters;
    }

    @Bean("fixedFileTokenizer")
    public FixedLengthTokenizer getFixedLengthTokenizer(){
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("ISIN", "Quantity", "Price", "Customer");
        tokenizer.setColumns(
                new Range(1,12),
                new Range(13,15),
                new Range(16,20),
                new Range(21,29)
        );
        return tokenizer;
    }

    @Bean("fieldSetMapper")
    public TradeFieldSetMapper getTradeFieldSetMapper(){
        TradeFieldSetMapper mapper = new TradeFieldSetMapper();
        return mapper;
    }

    @Bean("itemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("jobParameters") JobParameters jobParameters
            ,@Qualifier("fixedFileTokenizer") FixedLengthTokenizer fixedFileTokenizer
            ,@Qualifier("fieldSetMapper") TradeFieldSetMapper fieldSetMapper
    ){
        FlatFileItemReader itemReader = new FlatFileItemReader();
        itemReader.setSaveState(true);

        String inputFile = "data/demo13/TradeDataStep.txt";
        inputFile = jobParameters.getString("input.file");
        ClassPathResource inputResource = new ClassPathResource(inputFile,this.getClass().getClassLoader());
        itemReader.setResource(inputResource);

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(fixedFileTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        itemReader.setLineMapper(lineMapper);

        return itemReader;
    }

    @Bean("step1")
    public Step getStep1(
            @Qualifier("itemReader") FlatFileItemReader itemReader
            ,@Qualifier("processor") ValidatingItemProcessor processor
            ,@Qualifier("tradeWriter") TradeWriter tradeWriter
    ){
        StepBuilder stepBuilder = stepBuilderFactory.get("step");

        SimpleStepBuilder builder = stepBuilder.chunk(2); // commit-interval="2"
        builder.reader(itemReader);
        builder.processor(processor);
        builder.writer(tradeWriter);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

}
