package com.official.demo17_multilineOrderJob.run;

import com.official.demo17_multilineOrderJob.extractor.*;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Build_multilineOrderOutputAggregators {
    @Bean("outputAggregators")
    public Map<String, FormatterLineAggregator> getMap(){
        FormatterLineAggregator outputHeader = new FormatterLineAggregator();
        outputHeader.setFormat("%-12s%-10s%-30s");
        outputHeader.setFieldExtractor(new HeaderFieldExtractor());

        FormatterLineAggregator outputFooter = new FormatterLineAggregator();
        outputFooter.setFormat("%-10s%20s");
        outputFooter.setFieldExtractor(new FooterFieldExtractor());

        FormatterLineAggregator outputCustomer = new FormatterLineAggregator();
        outputCustomer.setFormat("%-9s%-10s%-10s%-10s%-10s");
        outputCustomer.setFieldExtractor(new CustomerFieldExtractor());

        FormatterLineAggregator outputAddress = new FormatterLineAggregator();
        outputAddress.setFormat("%-8s%-20s%-10s%-10s");
        outputAddress.setFieldExtractor(new AddressFieldExtractor());

        FormatterLineAggregator outputBilling = new FormatterLineAggregator();
        outputBilling.setFormat("%-8s%-10s%-20s");
        outputBilling.setFieldExtractor(new BillingInfoFieldExtractor());

        FormatterLineAggregator outputLineItem = new FormatterLineAggregator();
        outputLineItem.setFormat("%-5s%-10s%-10s");
        outputLineItem.setFieldExtractor(new LineItemFieldExtractor());

        Map<String, FormatterLineAggregator> map = new HashMap<>();
        map.put("header", outputHeader);
        map.put("footer", outputFooter);
        map.put("customer", outputCustomer);
        map.put("address", outputAddress);
        map.put("billing", outputBilling);
        map.put("item", outputLineItem);

        return map;
    }
}
