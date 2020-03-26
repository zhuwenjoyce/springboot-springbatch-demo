package com.official.demo9_headerFooterSample.run;

import com.official.demo9_headerFooterSample.domain.HeaderCopyCallback;
import com.official.demo9_headerFooterSample.domain.SummaryFooterCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildWriter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Value("${file.output.directory}")
    private String fileOutputDirectoryStr;

    @Bean("outputResource")
    public FileSystemResource getFileSystemResource(){
        File file = new File(fileOutputDirectoryStr + "demo9_output/");
        if(!file.exists()){
            file.mkdirs();
        }

        FileSystemResource resource = new FileSystemResource(file.getAbsolutePath() + "/headerFooterOutput.txt");
        return resource;
    }

    @Bean("writer")
    public FlatFileItemWriter getFlatFileItemWriter(
            @Qualifier("outputResource") FileSystemResource outputResource
            ,@Qualifier("headerCopier") HeaderCopyCallback headerCopier
            ,@Qualifier("footerCallback") SummaryFooterCallback footerCallback
    ){
        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setResource(outputResource);
        writer.setLineAggregator(new PassThroughLineAggregator());
        writer.setHeaderCallback(headerCopier);
        writer.setFooterCallback(footerCallback);
        return writer;
    }

}
