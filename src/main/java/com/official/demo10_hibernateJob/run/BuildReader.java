package com.official.demo10_hibernateJob.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildReader {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("hibernateItemReader")
    public HibernateCursorItemReader getHibernateCursorItemReader(
            @Qualifier("hibernateSessionFactory") LocalSessionFactoryBean hibernateSessionFactory
    ){
        HibernateCursorItemReader reader = new HibernateCursorItemReader();
        reader.setQueryString("select id,name,credit from CustomerCredit");
        reader.setSessionFactory(hibernateSessionFactory.getObject());
        reader.setFetchSize(1);  // read one record at a time。每次只读取一条
        return reader;
    }
}
