package com.mydemo.demo2.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyJob1 implements Job {

    private static Logger logger = LoggerFactory.getLogger(MyJob1.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String getName() {
        return "myJob1";
    }

    @Override
    public boolean isRestartable() {
        return false;
    }

    @Override
    public void execute(JobExecution execution) {
        logger.info("["+sdf.format(new Date())+"]MyJob1的execute方法执行啦~~~ JobParameter.time: " + sdf.format( execution.getJobParameters().getLong("time")));
    }

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();
        return runIdIncrementer;
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        JobParametersValidator validator = new DefaultJobParametersValidator();
        return validator;
    }
}
