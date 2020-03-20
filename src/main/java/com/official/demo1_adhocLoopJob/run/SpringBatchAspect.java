package com.official.demo1_adhocLoopJob.run;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class SpringBatchAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //What kind of method calls I would intercept
    //execution(* PACKAGE.*.*(..))
    //Weaving & Weaver
    @Before("execution( * org.springframework.batch..Step+.execute(..))")
    public void before(JoinPoint joinPoint) {
        logger.info(" SpringBatchAspect before::: {}", joinPoint);
    }
}
