package com.official.demo15_mailJob.run;

import com.official.demo15_mailJob.domain.TestMailErrorHandler;
import com.official.demo15_mailJob.domain.TestMailSender;
import com.official.demo15_mailJob.domain.User;
import com.official.demo15_mailJob.domain.UserMailItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.mail.SimpleMailMessageItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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

    @Bean("mailSender")
    public TestMailSender getTestMailSender(){
        List<String> subjects = new ArrayList<>();
        subjects.add("John Adams's Account Info");
        subjects.add("James Madison's Account Info");

        TestMailSender sender = new TestMailSender();
        sender.setSubjectsToFail(subjects);
        return sender;
    }

    @Bean("loggingMailErrorHandler")
    public TestMailErrorHandler getTestMailErrorHandler(){
        TestMailErrorHandler handler = new TestMailErrorHandler();
        return handler;
    }

    @Bean("itemProcessor")
    public UserMailItemProcessor getUserMailItemProcessor(){
        UserMailItemProcessor processor = new UserMailItemProcessor();
        return processor;
    }

    @Bean("itemWriter")
    public SimpleMailMessageItemWriter getSimpleMailMessageItemWriter(
            @Qualifier("mailSender") TestMailSender mailSender
            ,@Qualifier("loggingMailErrorHandler") TestMailErrorHandler loggingMailErrorHandler
    ){
        SimpleMailMessageItemWriter itemWriter = new SimpleMailMessageItemWriter();
        itemWriter.setMailSender(mailSender);
        itemWriter.setMailErrorHandler(loggingMailErrorHandler);
        return itemWriter;
    }

    @Bean("itemReader")
    public JdbcCursorItemReader getJdbcCursorItemReader(
            @Qualifier("oracleDataSource") DataSource oracleDataSource
    ){
        JdbcCursorItemReader itemReader = new JdbcCursorItemReader();
        itemReader.setDataSource(oracleDataSource);
        itemReader.setVerifyCursorPosition(true);
        itemReader.setSql("select ID, NAME, EMAIL from USERS");
        itemReader.setRowMapper(new BeanPropertyRowMapper(User.class));
        return itemReader;
    }
    @Bean("step1")
    public TaskletStep buildStep1(
            @Qualifier("itemReader") JdbcCursorItemReader itemReader
            ,@Qualifier("itemProcessor") UserMailItemProcessor itemProcessor
            ,@Qualifier("itemWriter") SimpleMailMessageItemWriter itemWriter
    ){
        TaskletStep taskletStep = this.stepBuilderFactory.get("step1")
                .<String, String>chunk(5)  // commit-interval="5"
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skipLimit(100)  // :: skip-limit="100"
                /*
                <skippable-exception-classes>
						<include class="java.lang.RuntimeException"/>
				</skippable-exception-classes>
                 */
                .skip(RuntimeException.class)
                .build();
        taskletStep.setAllowStartIfComplete(true);
        return taskletStep;
    }
}
