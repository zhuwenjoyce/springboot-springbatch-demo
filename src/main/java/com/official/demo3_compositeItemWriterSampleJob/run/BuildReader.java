package com.official.demo3_compositeItemWriterSampleJob.run;

import com.official.demo3_compositeItemWriterSampleJob.job.TradeFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;

@Configuration
public class BuildReader {

    @Bean("fixedFileTokenizer")
    public FixedLengthTokenizer getFixedLengthTokenizer(){
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("ISIN", "Quantity", "Price", "Customer");
        tokenizer.setColumns(
                new Range(1,12),
                new Range(13,15),
                new Range(16,20),
                new Range(21,29)
        );
        return tokenizer;
    }

    @Bean("lineMapper")
    public DefaultLineMapper getDefaultLineMapper(
            @Qualifier("fixedFileTokenizer") FixedLengthTokenizer fixedFileTokenizer
    ){
        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(fixedFileTokenizer);
        lineMapper.setFieldSetMapper(new TradeFieldSetMapper());
        return lineMapper;
    }

    @Value("classpath:data/demo3/20070122.teststream.ImportTradeDataStep.txt")
    private Resource inputFile;

    @Bean("fileItemReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("lineMapper") DefaultLineMapper lineMapper
    ) throws FileNotFoundException {
        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(inputFile);
        reader.setLineMapper(lineMapper);
        return  reader;
    }
}
