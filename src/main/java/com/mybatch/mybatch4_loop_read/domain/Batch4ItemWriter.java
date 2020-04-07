package com.mybatch.mybatch4_loop_read.domain;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.time.LocalDateTime;
import java.util.List;

public class Batch4ItemWriter implements ItemWriter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void write(List items) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String nowStr = now.toString();
        logger.info(nowStr+"::::"+JSONObject.toJSONString(items));
        Thread.sleep(2000);
    }
}
