package com.official.demo2_beanWrapperMapperSampleJob.run;

import com.howtodoinjava.example1.run.ConfigJob;
import com.official.demo1_adhocLoopJob.job.InfiniteLoopWriter;
import com.official.demo2_beanWrapperMapperSampleJob.job.JdbcTradeDao;
import com.official.demo2_beanWrapperMapperSampleJob.job.TradeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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

    @Qualifier("dataSource")
    DataSource dataSource;

    @Bean("fixedValidator")
    public SpringValidator getSpringValidator(){
        SpringValidator validator = new SpringValidator();
        validator.setValidator(new TradeValidator());
        return  validator;
    }

    @Bean("processor")
    public ValidatingItemProcessor getValidatingItemProcessor(@Qualifier("fixedValidator") SpringValidator fixedValidator){
        return new ValidatingItemProcessor(fixedValidator);
    }

    @Bean("tradeTokenizer")
    public FixedLengthTokenizer getFixedLengthTokenizer(){
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("ISIN,Quantity,price, CUSTOMER");
        tokenizer.setColumns(
                new Range(1,12)
                ,new Range(13,15)
                ,new Range(16,20)
                ,new Range(21,29)
        );
        return tokenizer;
    }

    @Bean("tradeFieldSetMapper")
    public BeanWrapperFieldSetMapper getBeanWrapperFieldSetMapper(){
        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper();
        mapper.setPrototypeBeanName("trade");
        return mapper;
    }

    @Bean("tradeFileItemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("tradeTokenizer") FixedLengthTokenizer tradeTokenizer
            ,@Qualifier("tradeFieldSetMapper") BeanWrapperFieldSetMapper tradeFieldSetMapper
    ){
        FlatFileItemReader reader = new FlatFileItemReader();

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource inputFile = resourceLoader.getResource("input/demo2_beanWrapperMapperJob/20070122.teststream.ImportTradeDataStep.txt");
        reader.setResource(inputFile);

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tradeTokenizer);
        lineMapper.setFieldSetMapper(tradeFieldSetMapper);
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean("tradeDao")
    public JdbcTradeDao getJdbcTradeDao(){
        JdbcTradeDao tradeDao = new JdbcTradeDao();
        tradeDao.setDataSource(dataSource);
        OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer = new OracleSequenceMaxValueIncrementer();
        oracleSequenceMaxValueIncrementer.setIncrementerName("TRADE_SEQ"); // TRADE_SEQ reference src/main/resources/init-sql/init-demo2.sql
        tradeDao.setIncrementer(oracleSequenceMaxValueIncrementer);
        return tradeDao;
    }

    @Bean("step1")
    public Step getStep1(@Qualifier("tradeFileItemReader") FlatFileItemReader tradeFileItemReader
            ,@Qualifier("writer") InfiniteLoopWriter writer
            ,@Qualifier("processor") ValidatingItemProcessor processor) {
        return this.stepBuilderFactory.get("step")
                .chunk(1) // 这里的chunkSize 就是 commit-interval <chunk reader="reader" writer="writer" commit-interval="5"/>
                .reader(tradeFileItemReader)
                .writer(writer)
                .processor(processor)
                .build();
    }
}
