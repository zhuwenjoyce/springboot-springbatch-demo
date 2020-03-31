package com.official.demo6_footballJob.run;

import com.official.demo6_footballJob.domain.step2.GameFieldSetMapper;
import com.official.demo6_footballJob.domain.step2.JdbcGameDao;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class BuildStep2GameLoad {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:data/demo6/games-small.csv")
    private Resource inputResource;

    @Bean("gameFileItemReader")
    public FlatFileItemReader getFlatFileItemReader(){
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("id","year","team","week","opponent","completes","attempts","passingYards","passingTd","interceptions","rushes","rushYards","receptions","receptionYards","totalTd");

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new GameFieldSetMapper());

        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(inputResource);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean("gameWriter")
    public JdbcGameDao getJdbcGameDao(
            @Qualifier("oracleDataSource") DataSource dataSource
    ){
        JdbcGameDao gameDao = new JdbcGameDao();
        gameDao.setDataSource(dataSource);
        return gameDao;
    }

    @Bean("gameLoadStep")
    public Step getPlayerloadStep(
            @Qualifier("gameFileItemReader") FlatFileItemReader gameFileItemReader
            ,@Qualifier("gameWriter") JdbcGameDao gameWriter
    ){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "gameLoadStepName-" + uuid;
        StepBuilder stepBuilder = stepBuilderFactory.get(jobName);

        SimpleStepBuilder builder = stepBuilder.chunk(2);
        builder.reader(gameFileItemReader);
        builder.writer(gameWriter);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

}
