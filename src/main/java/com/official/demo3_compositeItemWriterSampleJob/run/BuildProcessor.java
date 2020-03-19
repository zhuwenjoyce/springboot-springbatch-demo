package com.official.demo3_compositeItemWriterSampleJob.run;

import com.official.demo3_compositeItemWriterSampleJob.job.TradeValidator;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuildProcessor {

    @Bean("fixedValidator")
    public SpringValidator getSpringValidator(){
        SpringValidator validator = new SpringValidator();
        validator.setValidator(new TradeValidator());
        return validator;
    }

    @Bean("processor")
    public ValidatingItemProcessor getValidatingItemProcessor(
            @Qualifier("fixedValidator") SpringValidator fixedValidator
    ){
        ValidatingItemProcessor processor = new ValidatingItemProcessor(fixedValidator);
        return processor;
    }
}
