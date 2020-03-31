package com.official.demo3_compositeItemWriterSampleJob.run;

import com.official.demo3_compositeItemWriterSampleJob.job.JdbcTradeDao;
import com.official.demo3_compositeItemWriterSampleJob.job.TradeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BuildWriter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${file.output.directory}")
    private String fileOutputDirectory;

    @Bean("fileItemWriter1")
    public FlatFileItemWriter getFlatFileItemWriter1() throws IOException {
        File outputDirectory = new File(fileOutputDirectory + "demo3_output/");
        if(!outputDirectory.exists()){
            Boolean isCreate =  outputDirectory.mkdirs();
            logger.info("创建新文件夹成功：" + outputDirectory.getAbsolutePath());
        }

        String outputFile = outputDirectory + "/demo3_CustomerReport1.txt";

        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setName("fw1");
        writer.setResource(new FileSystemResource(outputFile));
        writer.setLineAggregator(new PassThroughLineAggregator());
        return writer;
    }

    @Bean("fileItemWriter2")
    public FlatFileItemWriter getFlatFileItemWriter2() throws IOException {
        File outputDirectory = new File(fileOutputDirectory + "demo3_output/");
        if(!outputDirectory.exists()){
            Boolean isCreate =  outputDirectory.mkdirs();
            logger.info("创建新文件夹成功：" + outputDirectory.getAbsolutePath());
        }

        String outputFile = outputDirectory + "/demo3_CustomerReport2.txt";

        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setName("fw2");
        writer.setResource(new FileSystemResource(outputFile));
        writer.setLineAggregator(new PassThroughLineAggregator());
        return writer;
    }

    @Bean("tradeDao")
    public JdbcTradeDao getJdbcTradeDao(
           @Qualifier("oracleDataSource") DataSource dataSource
    ){
        JdbcTradeDao tradeDao = new JdbcTradeDao();
        tradeDao.setDataSource(dataSource);
        OracleSequenceMaxValueIncrementer oracleIncrementer = new OracleSequenceMaxValueIncrementer();
        oracleIncrementer.setIncrementerName("TRADE_SEQ"); // TRADE_SEQ reference src/main/resources/init-sql/init-demo3.sql
        oracleIncrementer.setDataSource(dataSource);
        tradeDao.setIncrementer(oracleIncrementer);
        return tradeDao;
    }

    @Bean("compositeWriter")
    public CompositeItemWriter getCompositeItemWriter(
            @Qualifier("tradeDao") JdbcTradeDao tradeDao
            ,@Qualifier("fileItemWriter1") FlatFileItemWriter fileItemWriter1
            ,@Qualifier("fileItemWriter2") FlatFileItemWriter fileItemWriter2
    ){
        CompositeItemWriter writer = new CompositeItemWriter();
        List<ItemWriter> delegates = new ArrayList<>();

        TradeWriter tradeWriter = new TradeWriter();
        tradeWriter.setDao(tradeDao);
        delegates.add(tradeWriter);
        delegates.add(fileItemWriter1);
        delegates.add(fileItemWriter2);

        writer.setDelegates(delegates);
        return writer;
    }
}
