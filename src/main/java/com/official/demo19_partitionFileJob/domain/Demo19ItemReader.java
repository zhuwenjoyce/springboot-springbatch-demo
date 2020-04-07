package com.official.demo19_partitionFileJob.domain;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;

public class Demo19ItemReader extends FlatFileItemReader {

    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
    }

}
