package com.official.demo1_adhocLoopJob.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class Step1 implements Tasklet {
    private static Logger logger = LoggerFactory.getLogger(Step1.class);

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Step1 execute.........");
        return RepeatStatus.FINISHED;
    }
}