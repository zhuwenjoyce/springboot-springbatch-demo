package com.official.demo1_adhocLoopJob.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MyTasklet implements Tasklet {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info(" MyTasklet 执行了......");
        return RepeatStatus.FINISHED;
    }
}