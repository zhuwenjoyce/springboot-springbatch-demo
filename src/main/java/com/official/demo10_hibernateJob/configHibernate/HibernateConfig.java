package com.official.demo10_hibernateJob.configHibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean("hibernateSessionFactory")
    public LocalSessionFactoryBean getLocalSessionFactoryBean(
            @Qualifier("dataSource") DataSource dataSource
    ){
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.format_sql","true");

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(properties);
        sessionFactory.setMappingResources("data/demo10/mapping/CustomerCredit.hbm.xml");

        logger.info("----------------");

        return sessionFactory;
    }

    @Bean("hibernateTransactionManager")
    public HibernateTransactionManager getHibernateTransactionManager(
            @Qualifier("hibernateSessionFactory") LocalSessionFactoryBean hibernateSessionFactory
    ){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(hibernateSessionFactory.getObject());
        return transactionManager;
    }

}
