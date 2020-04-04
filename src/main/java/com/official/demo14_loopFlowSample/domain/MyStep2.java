package com.official.demo14_loopFlowSample.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;

public class MyStep2 implements Step {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public String getName() {
        return "step2Name";
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return true;
    }

    @Override
    public int getStartLimit() {
        return 1;
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {
        logger.info("["+this.getName() + "]执行啦~~~~~");
    }
}
