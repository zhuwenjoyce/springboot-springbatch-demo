package com.mybatch.mybatch1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Demo1Controller {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("classpath:logback-spring.xml.backup")
    private Resource logbackResourceFile;

    @RequestMapping("/demo1/file/ai-te-value/logback")
    public Map getLogbackFileByValue() throws IOException {
        Map map = new HashMap();
        map.put("@Value logbackFile",logbackResourceFile.toString());
        map.put("file name",logbackResourceFile.getFile().getName());
        map.put("current time", sdf.format(new Date()));
        return map;
    }

    @RequestMapping("/demo1/file/ResourceLoader/logback")
    public Map getLogbackFileByResourceLoader() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource logbackFile = resourceLoader.getResource("logback-spring.xml.backup");
        Map map = new HashMap();
        map.put("resourceLoader logbackFile",logbackFile.toString());
        map.put("file name",logbackFile.getFile().getName());
        map.put("current time", sdf.format(new Date()));
        return map;
    }

}
