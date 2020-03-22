package com.official.demo6_footballJob.run;

import com.official.demo6_footballJob.domain.step1.JdbcPlayerDao;
import com.official.demo6_footballJob.domain.step1.PlayerFieldSetMapper;
import com.official.demo6_footballJob.domain.step1.PlayerItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep1Playerload {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobLauncher jobLauncher;

    @Value("classpath:data/demo6/player-small1.csv")
    private Resource inputFile;

    @Bean("playerFileItemReader")
    public FlatFileItemReader getFlatFileItemReader(){
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("ID","lastName","firstName","position","birthYear","debutYear");

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PlayerFieldSetMapper());

        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(inputFile);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean("playerWriter")
    public PlayerItemWriter getPlayerItemWriter(
            @Qualifier("dataSource") DataSource dataSource
    ){
        PlayerItemWriter writer = new PlayerItemWriter();
        JdbcPlayerDao playerDao = new JdbcPlayerDao();
        playerDao.setDataSource(dataSource);
        writer.setPlayerDao(playerDao);
        return writer;
    }

    @Bean("playerloadStep")
    public Step getPlayerloadStep(
            @Qualifier("playerFileItemReader") FlatFileItemReader playerFileItemReader
            ,@Qualifier("playerWriter") PlayerItemWriter playerWriter
    ){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "playerloadStepName-" + uuid;
        StepBuilder stepBuilder = stepBuilderFactory.get(jobName);

        SimpleStepBuilder builder = stepBuilder.chunk(2);
        builder.reader(playerFileItemReader);
        builder.writer(playerWriter);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }
}
