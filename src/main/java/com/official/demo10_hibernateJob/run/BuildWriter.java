package com.official.demo10_hibernateJob.run;

import com.official.demo10_hibernateJob.domain.HibernateAwareCustomerCreditItemWriter;
import com.official.demo10_hibernateJob.domain.HibernateCreditDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildWriter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("customerCreditDao")
    public HibernateCreditDao getHibernateCreditDao(){
        HibernateCreditDao dao = new HibernateCreditDao();
        return dao;
    }

    @Bean("hibernateCreditWriter")
    public HibernateAwareCustomerCreditItemWriter getHibernateAwareCustomerCreditItemWriter(
            @Qualifier("customerCreditDao") HibernateCreditDao customerCreditDao
            ,@Qualifier("hibernateSessionFactory") LocalSessionFactoryBean hibernateSessionFactory
    ){
        HibernateAwareCustomerCreditItemWriter writer = new HibernateAwareCustomerCreditItemWriter();
        writer.setDao(customerCreditDao);
        writer.setSessionFactory(hibernateSessionFactory.getObject());
        return writer;
    }
}
