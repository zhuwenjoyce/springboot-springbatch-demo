package com.mydemo.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileNotFoundException;

@SpringBootApplication
public class Demo1Application {
	
	private static Logger logger = LoggerFactory.getLogger(Demo1Application.class);

	public static void main(String[] args) throws FileNotFoundException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo1Application.class, args);
		logger.info("demo 1 SpringBoot 启动成功！");

		SimpleJobLauncher jobLaunch = (SimpleJobLauncher) applicationContext.getBean(SimpleJobLauncher.class);
		if(null != jobLaunch){
			logger.info("jobLaunch 对象创建成功: " + jobLaunch.toString());
		}else {
			logger.info("jobLaunch 对象创建失败！！！");
		}
	}

}
