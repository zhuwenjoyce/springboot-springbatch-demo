package com.official.demo4_customerFilterJob.run;

import com.official.demo4_customerFilterJob.dao.JdbcCustomerDao;
import com.official.demo4_customerFilterJob.domain.CustomerUpdateProcessor;
import com.official.demo4_customerFilterJob.domain.CustomerUpdateWriter;
import com.official.demo4_customerFilterJob.listener.CompositeCustomerUpdateLineTokenizer;
import com.official.demo4_customerFilterJob.log.CommonsLoggingInvalidCustomerLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("customerDao")
    public JdbcCustomerDao getJdbcCustomerDao(
            @Qualifier("dataSource") DataSource dataSource
    ){
        OracleSequenceMaxValueIncrementer oracleIncrementer = new OracleSequenceMaxValueIncrementer();
        oracleIncrementer.setIncrementerName("CUSTOMER_SEQ"); // CUSTOMER_SEQ reference src/main/resources/init-sql/init-demo4.sql
        oracleIncrementer.setDataSource(dataSource);

        JdbcCustomerDao dao = new JdbcCustomerDao();
        dao.setDataSource(dataSource);
        dao.setIncrementer(oracleIncrementer);
        return dao;
    }

    @Bean("customerUpdateProcessor")
    public CustomerUpdateProcessor getCustomerUpdateProcessor(
            @Qualifier("customerDao") JdbcCustomerDao customerDao
    ){
        CustomerUpdateProcessor processor = new CustomerUpdateProcessor();
        processor.setCustomerDao(customerDao);
        processor.setInvalidCustomerLogger(new CommonsLoggingInvalidCustomerLogger());
        return processor;
    }

    @Bean("customerUpdateWriter")
    public CustomerUpdateWriter getCustomerUpdateWriter(
            @Qualifier("customerDao") JdbcCustomerDao customerDao){
        CustomerUpdateWriter writer = new CustomerUpdateWriter();
        writer.setCustomerDao(customerDao);
        return writer;
    }

    @Bean("step1")
    public Step getStep1(@Qualifier("customerFileUploadReader") FlatFileItemReader customerFileUploadReader
            ,@Qualifier("customerUpdateProcessor") CustomerUpdateProcessor customerUpdateProcessor
            ,@Qualifier("customerUpdateWriter") CustomerUpdateWriter customerUpdateWriter
            ,@Qualifier("customerTokenizer") CompositeCustomerUpdateLineTokenizer customerTokenizer
    ) {

        StepBuilder stepBuilder = stepBuilderFactory.get("step");

        SimpleStepBuilder builder = stepBuilder.chunk(1);
        builder.reader(customerFileUploadReader);
        builder.processor(customerUpdateProcessor);
        builder.writer(customerUpdateWriter);
        builder.listener(customerTokenizer);

        TaskletStep taskletStep = builder.build();

        return taskletStep;
    }
}
