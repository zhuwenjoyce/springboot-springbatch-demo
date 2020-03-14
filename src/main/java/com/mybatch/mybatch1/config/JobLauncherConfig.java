package com.mybatch.mybatch1.config;

import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import javax.sql.DataSource;

/**
 * 这个类就为组建@Bean("jobLauncher")对象，此举与以下代码相等：
 * 类加上注解：@EnableBatchProcessing，可自动获得类注入
 * @Autowired
 * private JobLauncher jobLauncher;
 */
@Configuration
public class JobLauncherConfig {

    @Bean("jdbcJobInstanceDao")
    public JdbcJobInstanceDao getJdbcJobInstanceDao(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){
        JdbcJobInstanceDao jobInstanceDao = new JdbcJobInstanceDao();
        jobInstanceDao.setJdbcTemplate(jdbcTemplate);
        jobInstanceDao.setJobIncrementer(new OracleSequenceMaxValueIncrementer());
        return jobInstanceDao;
    }

    @Bean("jdbcJobExecutionDao")
    public JdbcJobExecutionDao getJdbcJobExecutionDao(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){
        JdbcJobExecutionDao jobExecutionDao = new JdbcJobExecutionDao();
        jobExecutionDao.setJdbcTemplate(jdbcTemplate);
        jobExecutionDao.setJobExecutionIncrementer(new OracleSequenceMaxValueIncrementer());
        return jobExecutionDao;
    }

    @Bean("jdbcStepExecutionDao")
    public JdbcStepExecutionDao getJdbcStepExecutionDao(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){
        JdbcStepExecutionDao stepExecutionDao = new JdbcStepExecutionDao();
        stepExecutionDao.setJdbcTemplate(jdbcTemplate);
        stepExecutionDao.setStepExecutionIncrementer(new OracleSequenceMaxValueIncrementer());
        return stepExecutionDao;
    }

    @Bean("jdbcExecutionContextDao")
    public JdbcExecutionContextDao getJdbcExecutionContextDao(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){
        JdbcExecutionContextDao executionContextDao = new JdbcExecutionContextDao();
        executionContextDao.setJdbcTemplate(jdbcTemplate);
        executionContextDao.setSerializer(new Jackson2ExecutionContextStringSerializer());
        return executionContextDao;
    }

    @Bean("jobRepositoryFactoryBean")
    public JobRepositoryFactoryBean getJobRepositoryFactoryBean(@Qualifier("dataSource") DataSource dataSource,
                                                                @Qualifier("txManager") DataSourceTransactionManager txManager){
        JobRepositoryFactoryBean jobRepository = new JobRepositoryFactoryBean();
        jobRepository.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        jobRepository.setDataSource(dataSource);
        jobRepository.setTransactionManager(txManager);
        return jobRepository;
    }

    @Bean("jobRepository")
    public SimpleJobRepository getSimpleJobRepository(@Qualifier("jdbcJobInstanceDao") JdbcJobInstanceDao jdbcJobInstanceDao
        ,@Qualifier("jdbcJobExecutionDao") JdbcJobExecutionDao jdbcJobExecutionDao
        ,@Qualifier("jdbcStepExecutionDao") JdbcStepExecutionDao jdbcStepExecutionDao
        ,@Qualifier("jdbcExecutionContextDao") JdbcExecutionContextDao jdbcExecutionContextDao ) {
    	SimpleJobRepository jobRepository = new SimpleJobRepository(jdbcJobInstanceDao, jdbcJobExecutionDao, jdbcStepExecutionDao, jdbcExecutionContextDao);
    	return jobRepository;
    }

    @Bean("jobLauncher")
    public SimpleJobLauncher getSimpleJobLauncher(@Qualifier("jobRepository") SimpleJobRepository jobRepository){
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }

    @Bean("jobExplorer")
    public SimpleJobExplorer getSimpleJobExplorer(@Qualifier("jdbcJobInstanceDao") JdbcJobInstanceDao jdbcJobInstanceDao
            ,@Qualifier("jdbcJobExecutionDao") JdbcJobExecutionDao jdbcJobExecutionDao
            ,@Qualifier("jdbcStepExecutionDao") JdbcStepExecutionDao jdbcStepExecutionDao
            ,@Qualifier("jdbcExecutionContextDao") JdbcExecutionContextDao jdbcExecutionContextDao ){
        SimpleJobExplorer jobExplorer = new SimpleJobExplorer(jdbcJobInstanceDao,jdbcJobExecutionDao,jdbcStepExecutionDao,jdbcExecutionContextDao);
        return jobExplorer;
    }

    @Bean("jobRegistry")
    public MapJobRegistry getMapJobRegistry(){
        MapJobRegistry jobRegistry = new MapJobRegistry();
        return  jobRegistry;
    }

    @Bean("jobOperator")
    public SimpleJobOperator getSimpleJobOperator(@Qualifier("jobLauncher") SimpleJobLauncher jobLauncher
    ,@Qualifier("jobExplorer") SimpleJobExplorer jobExplorer
    ,@Qualifier("jobRepository") SimpleJobRepository jobRepository
    ,@Qualifier("jobRegistry") MapJobRegistry jobRegistry){
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        return jobOperator;
    }
}
