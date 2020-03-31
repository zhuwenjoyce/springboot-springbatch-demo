/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.official.demo11_infiniteLoopJob.domain;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Joyce Zhu
 *
 */
public class DummyItemWriter implements ItemWriter<Trade> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static int count = 0;

	@Override
	public void write(List<? extends Trade> items) throws Exception {
		if(CollectionUtils.isEmpty(items)){
			logger.info("writer没有读到任何值！");
			return;
		}
		for(Trade trade : items){
			logger.info("writer["+(++count)+"]=======" + JSONObject.toJSONString(trade));
		}
		Thread.sleep(500);
	}

}
