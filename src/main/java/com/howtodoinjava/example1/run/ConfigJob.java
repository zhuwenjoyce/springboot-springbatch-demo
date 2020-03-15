package com.howtodoinjava.example1.run;

import com.howtodoinjava.example1.job.MyTaskOne;
import com.howtodoinjava.example1.job.MyTaskTwo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class ConfigJob {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean("step1")
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet(new MyTaskOne())
                .build();
    }

    @Bean("step2")
    public Step stepTwo(){
        return steps.get("stepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }

    /**
     * job一旦注册为bean，SpringBatch框架会自动运行这个job，如果再使用jobLauncher.run(job, params);方式启动job，就会导致job被重复调用
     * @param step1
     * @param step2
     * @return
     */
    @Bean
    public Job demoJob(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2 ){
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .build();
    }
}
