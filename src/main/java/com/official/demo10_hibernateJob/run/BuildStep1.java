package com.official.demo10_hibernateJob.run;

import com.official.demo10_hibernateJob.domain.CustomerCreditIncreaseProcessor;
import com.official.demo10_hibernateJob.domain.HibernateAwareCustomerCreditItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildStep1 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("step1")
    public Step getPlayerloadStep(
            @Qualifier("hibernateItemReader") HibernateCursorItemReader hibernateItemReader
            ,@Qualifier("creditIncreaseProcessor") CustomerCreditIncreaseProcessor creditIncreaseProcessor
            ,@Qualifier("hibernateCreditWriter") HibernateAwareCustomerCreditItemWriter hibernateCreditWriter
    ){
        Step step = this.stepBuilderFactory.get("step1")
                .<String, String>chunk(1)
                .reader(hibernateItemReader)
                .processor(creditIncreaseProcessor)
                .writer(hibernateCreditWriter)
                .faultTolerant()
                .skipLimit(5)  // :: skip-limit="5"
                /*
                <skippable-exception-classes>
						<include class="java.lang.RuntimeException"/>
				</skippable-exception-classes>
                 */
                .skip(RuntimeException.class)
                .build();
        return step;
    }

}
