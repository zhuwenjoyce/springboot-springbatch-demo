package com.official.demo17_multilineOrderJob.run;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * build spring-batch\spring-batch-samples\src\main\resources\jobs\multilineOrderInputTokenizers.xml
 */
@Configuration
public class Build_multilineOrderInputTokenizers {
    @Bean("orderFileTokenizer")
    public PatternMatchingCompositeLineTokenizer getPatternMatchingCompositeLineTokenizer(){
        DelimitedLineTokenizer headerRecordTokenizer = new DelimitedLineTokenizer();
        headerRecordTokenizer.setDelimiter(";");
        headerRecordTokenizer.setNames("LINE_ID","ORDER_ID","ORDER_DATE");

        DelimitedLineTokenizer footerRecordTokenizer = new DelimitedLineTokenizer();
        footerRecordTokenizer.setDelimiter(";");
        footerRecordTokenizer.setNames("LINE_ID","TOTAL_LINE_ITEMS","TOTAL_ITEMS","TOTAL_PRICE");

        DelimitedLineTokenizer businessCustomerLineTokenizer = new DelimitedLineTokenizer();
        businessCustomerLineTokenizer.setDelimiter(";");
        businessCustomerLineTokenizer.setNames("LINE_ID","COMPANY_NAME","REG_ID","VIP");

        DelimitedLineTokenizer customerLineTokenizer = new DelimitedLineTokenizer();
        customerLineTokenizer.setDelimiter(";");
        customerLineTokenizer.setNames("LINE_ID","LAST_NAME","FIRST_NAME","MIDDLE_NAME","REGISTERED","REG_ID","VIP");

        DelimitedLineTokenizer billingAddressLineTokenizer = new DelimitedLineTokenizer();
        billingAddressLineTokenizer.setDelimiter(";");
        billingAddressLineTokenizer.setNames("LINE_ID","ADDRESSEE","ADDR_LINE1","ADDR_LINE2","CITY","ZIP_CODE","STATE","COUNTRY");

        DelimitedLineTokenizer shippingAddressLineTokenizer = new DelimitedLineTokenizer();
        shippingAddressLineTokenizer.setDelimiter(";");
        shippingAddressLineTokenizer.setNames("LINE_ID","ADDRESSEE","ADDR_LINE1","ADDR_LINE2","CITY","ZIP_CODE","STATE","COUNTRY");

        DelimitedLineTokenizer billingLineTokenizer = new DelimitedLineTokenizer();
        billingLineTokenizer.setDelimiter(";");
        billingLineTokenizer.setNames("LINE_ID","PAYMENT_TYPE_ID","PAYMENT_DESC");

        DelimitedLineTokenizer shippingLineTokenizer = new DelimitedLineTokenizer();
        shippingLineTokenizer.setDelimiter(";");
        shippingLineTokenizer.setNames("LINE_ID","SHIPPER_ID","SHIPPING_TYPE_ID","ADDITIONAL_SHIPPING_INFO");

        DelimitedLineTokenizer itemLineTokenizer = new DelimitedLineTokenizer();
        itemLineTokenizer.setDelimiter(";");
        itemLineTokenizer.setNames("LINE_ID","ITEM_ID","PRICE","DISCOUNT_PERC","DISCOUNT_AMOUNT","SHIPPING_PRICE","HANDLING_PRICE","QUANTITY","TOTAL_PRICE");

        DelimitedLineTokenizer defaultLineTokenizer = new DelimitedLineTokenizer();

        Map<String, LineTokenizer> map = new HashMap<>();
        map.put("HEA*", headerRecordTokenizer);
        map.put("FOT*", footerRecordTokenizer);
        map.put("BCU*", businessCustomerLineTokenizer);
        map.put("NCU*", customerLineTokenizer);
        map.put("BAD*", billingAddressLineTokenizer);
        map.put("SAD*", shippingAddressLineTokenizer);
        map.put("BIN*", billingLineTokenizer);
        map.put("SIN*", shippingLineTokenizer);
        map.put("LIT*", itemLineTokenizer);
        map.put("*", defaultLineTokenizer);

        PatternMatchingCompositeLineTokenizer tokenizer = new PatternMatchingCompositeLineTokenizer();
        tokenizer.setTokenizers(map);
        return tokenizer;
    }
}
