package com.official.demo18_parallelJob.run;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution( * org.springframework.batch.item.ItemWriter+.write(Object))")
    public Object profileAllMethods(JoinPoint joinPoint) throws Throwable{
        logger.info("LoggingAspect执行了~~~~");
        return null;
    }
}
