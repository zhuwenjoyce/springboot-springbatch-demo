package com.official.demo10_hibernateJob.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;

public class MyStartJob implements Step {
    private static Logger logger = LoggerFactory.getLogger(MyStartJob.class);
    @Override
    public String getName() {
        return "myStartJob";
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return true;
    }

    @Override
    public int getStartLimit() {
        return 100000;
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {
        logger.info("执行了：：：MyStartJob" );
    }
}
