package com.mybatch.mybatch4_loop_read.domain;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Batch4ItemReader implements ItemReader {
    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        for (int i = 0; i < 5; i++) {
            return "obj-" + i;
        }
        return null;
    }
}
