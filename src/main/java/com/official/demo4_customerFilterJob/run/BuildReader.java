package com.official.demo4_customerFilterJob.run;

import com.official.demo4_customerFilterJob.listener.CompositeCustomerUpdateLineTokenizer;
import com.official.demo4_customerFilterJob.domain.CustomerUpdateFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class BuildReader {

    @Value("classpath:data/demo4/customers.txt")
    private Resource inputResource;

    @Bean("customerMapper")
    public CustomerUpdateFieldSetMapper getCustomerUpdateFieldSetMapper(){
        return new CustomerUpdateFieldSetMapper();
    }

    @Bean("customerTokenizer")
    public CompositeCustomerUpdateLineTokenizer getCompositeCustomerUpdateLineTokenizer(){
        CompositeCustomerUpdateLineTokenizer tokenizer = new CompositeCustomerUpdateLineTokenizer();

        FixedLengthTokenizer customerTokenizer = new FixedLengthTokenizer();
        customerTokenizer.setColumns(
                new Range(1,1),
                new Range(2,18),
                new Range(19,26)
        );

        FixedLengthTokenizer footerTokenizer = new FixedLengthTokenizer();
        footerTokenizer.setColumns(
                new Range(1,1),
                new Range(2,8)
        );

        tokenizer.setCustomerTokenizer(customerTokenizer);
        tokenizer.setFooterTokenizer(footerTokenizer);
        return tokenizer;
    }

    @Bean("customerFileUploadReader")
    public FlatFileItemReader getFlatFileItemReader(
            @Qualifier("customerTokenizer") CompositeCustomerUpdateLineTokenizer customerTokenizer
            ,@Qualifier("customerMapper") CustomerUpdateFieldSetMapper customerMapper
    ){

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(customerTokenizer);
        lineMapper.setFieldSetMapper(customerMapper);

        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(inputResource);
        reader.setLineMapper(lineMapper);
        reader.setLinesToSkip(1);

        return reader;
    }
}
