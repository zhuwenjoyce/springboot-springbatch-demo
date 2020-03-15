package com.mybatch.mybatch1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;

/**
 * 这个例子演示 jobLauncher 和 JobRepository 对象是如何生成的，这个例子没有任何job
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.global", "com.mybatch.mybatch1"})
public class Mybatch1Application {
	
	private static Logger logger = LoggerFactory.getLogger(Mybatch1Application.class);

	public static void main(String[] args) throws FileNotFoundException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Mybatch1Application.class, args);
		logger.info("mybatch 1 SpringBoot 启动成功！");

		SimpleJobLauncher jobLaunch = (SimpleJobLauncher) applicationContext.getBean(SimpleJobLauncher.class);
		if(null != jobLaunch){
			logger.info("jobLaunch 对象创建成功: " + jobLaunch.toString());
		}else {
			logger.info("jobLaunch 对象创建失败！！！");
		}
	}

}
