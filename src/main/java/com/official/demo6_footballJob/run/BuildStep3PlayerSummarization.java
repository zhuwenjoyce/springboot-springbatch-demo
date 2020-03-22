package com.official.demo6_footballJob.run;

import com.official.demo6_footballJob.domain.step3.JdbcPlayerSummaryDao;
import com.official.demo6_footballJob.domain.step3.PlayerSummaryMapper;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@EnableBatchProcessing //可自动注入对象：jobBuilderFactory、stepBuilderFactory、jobLauncher
public class BuildStep3PlayerSummarization {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean("playerSummarizationSource")
    public JdbcCursorItemReader getJdbcCursorItemReader(
            @Qualifier("dataSource") DataSource dataSource
    ){
        JdbcCursorItemReader reader = new JdbcCursorItemReader();
        reader.setDataSource(dataSource);
        reader.setRowMapper(new PlayerSummaryMapper());
        reader.setSql("SELECT GAMES.player_id, GAMES.year_no, SUM(COMPLETES), SUM(ATTEMPTS), SUM(PASSING_YARDS), SUM(PASSING_TD), " +
                "SUM(INTERCEPTIONS), SUM(RUSHES), SUM(RUSH_YARDS), SUM(RECEPTIONS), SUM(RECEPTIONS_YARDS), SUM(TOTAL_TD) " +
                " from GAMES, PLAYERS where PLAYERS.player_id = GAMES.player_id group by GAMES.player_id, GAMES.year_no");
        reader.setVerifyCursorPosition(true);
        return reader;
    }

    @Bean("summaryWriter")
    public JdbcPlayerSummaryDao getJdbcPlayerSummaryDao(
            @Qualifier("dataSource") DataSource dataSource
    ){
        JdbcPlayerSummaryDao summaryDao = new JdbcPlayerSummaryDao();
        summaryDao.setDataSource(dataSource);
        return summaryDao;
    }

    @Bean("playerSummarizationStep")
    public Step getStep(
            @Qualifier("playerSummarizationSource") JdbcCursorItemReader playerSummarizationSource
            ,@Qualifier("summaryWriter") JdbcPlayerSummaryDao summaryWriter
    ){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String jobName = "playerSummarizationName-" + uuid;
        StepBuilder stepBuilder = stepBuilderFactory.get(jobName);

        SimpleStepBuilder builder = stepBuilder.chunk(2);
        builder.reader(playerSummarizationSource);
        builder.writer(summaryWriter);

        TaskletStep taskletStep = builder.build();
        return taskletStep;
    }

}
