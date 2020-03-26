package com.official.demo9_headerFooterSample.run;

import com.official.demo9_headerFooterSample.domain.HeaderCopyCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
@Component
public class BuildReader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean("inputResource")
    public ClassPathResource getClassPathResource() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/demo9/input.txt", this.getClass().getClassLoader());
        if(!resource.exists()){
            logger.error("resource.getURL()::: " + resource.getURL() + ", is not exists::: " + resource.exists());
            System.exit(1); // 1：非正常退出程序
        }
        return resource;
    }

    @Bean("headerCopier")
    public HeaderCopyCallback getHeaderCopyCallback(){
        HeaderCopyCallback callback = new HeaderCopyCallback();
        return callback;
    }

    @Bean("reader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("inputResource") ClassPathResource inputResource
            ,@Qualifier("headerCopier") HeaderCopyCallback headerCopier
    ){
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        PassThroughFieldSetMapper fieldSetMapper = new PassThroughFieldSetMapper();

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(inputResource);
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper);
        reader.setSkippedLinesCallback(headerCopier); // skip line as header field
        return reader;
    }
}
